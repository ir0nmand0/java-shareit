package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.model.dto.BookingState;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
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
        return patch(String.format("/%d?approved=%b",bookingId, approved), userId, null, null);
    }
}
