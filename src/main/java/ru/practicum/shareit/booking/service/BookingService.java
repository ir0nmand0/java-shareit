package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.*;

import java.util.Collection;

public interface BookingService {
    BookingDto create(final CreateBookingDto createBookingDto, final long userId);

    BookingDto update(final UpdateBookingDto updateBookingDto, final long userId);

    BookingDto patch(final PatchBookingDto patchBookingDto, final long id, final long userId);

    BookingDto updateBookingStatus(final long id, final boolean available, final long userId);

    Collection<BookingDto> findAllByUserId(final long id);

    BookingDto findOneById(final long id, final long userId);

    BookingDto findOneById(final long id);

    void deleteById(final long id);

    Collection<BookingDto> findAllById(final long userId, final State state);

    Collection<BookingDto> findAllByIdForOwner(final long userId, final State state);
}
