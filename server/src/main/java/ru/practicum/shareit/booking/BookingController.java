package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto findOneById(@PathVariable @Positive final long bookingId,
                                  @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return bookingService.findOneById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllById(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                              @RequestParam(required = false, defaultValue = "ALL") final State state) {
        return bookingService.findAllById(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByIdForOwner(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL") State state) {
        return bookingService.findAllByIdForOwner(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                             @Valid @RequestBody final CreateBookingDto createBookingDto) {
        return bookingService.create(createBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@PathVariable @Positive long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {

        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }
}
