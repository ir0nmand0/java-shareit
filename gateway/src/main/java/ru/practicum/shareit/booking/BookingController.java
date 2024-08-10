package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findOneById(@PathVariable @Positive final long bookingId,
                                              @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return bookingClient.findOneById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllById(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                              @RequestParam(required = false, defaultValue = "ALL") final State state) {
        return bookingClient.findAllById(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByIdForOwner(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL") State state) {
        return bookingClient.findAllByIdForOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                         @Valid @RequestBody final CreateBookingDto createBookingDto) {
        return bookingClient.create(createBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@PathVariable @Positive long bookingId,
                                               @RequestParam boolean approved,
                                               @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {

        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }
}
