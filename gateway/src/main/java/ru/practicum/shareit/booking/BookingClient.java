package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.client.BaseClient;

public class BookingClient extends BaseClient {
    public BookingClient(final RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findOneById(final long bookingId, final long userId) {
        return get(String.format("/%d", bookingId), userId);
    }

    public ResponseEntity<Object> findAllById(final long userId, final State state) {
        return get(String.format("?state=%s", state), userId);
    }

    public ResponseEntity<Object> findAllByIdForOwner(final long userId, final State state) {
        return get(String.format("/owner?state=%s", state), userId);
    }

    public ResponseEntity<Object> create(final CreateBookingDto createBookingDto, final long userId) {
        return post("", userId, createBookingDto);
    }

    public ResponseEntity<Object> updateBookingStatus(final long bookingId, final boolean approved, final long userId) {
        return patch(String.format("/%d?approved=%b", bookingId, approved), userId, null, null);
    }
}
