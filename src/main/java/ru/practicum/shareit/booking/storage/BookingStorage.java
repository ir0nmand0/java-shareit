package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;
import java.util.Optional;

public interface BookingStorage {
    Booking save(final Booking booking);

    void patch(final Booking booking);

    Optional<Booking> findOneById(final long id);

    Collection<Booking> findAllByBookerId(final long id);

    Optional<Booking> findOneById(final long id, final long userId);

    Booking findOneByIdOrElseThrow(final long id, final long userId);

    Booking findOneByIdOrElseThrow(final long id);

    void delete(final long id);

    Collection<Booking> findCurrentBookings(final long userId);

    Collection<Booking> findPastBookings(final long userId);

    Collection<Booking> findFutureBookings(final long userId);

    Collection<Booking> findBookingsByBookerIdAndStatus(final long userId, final BookingStatus status);

    Collection<Booking> findCurrentForOwner(final long userId);

    Collection<Booking> findPastForOwner(final long userId);

    Collection<Booking> findFutureForOwner(final long userId);

    Collection<Booking> findByBookerIdAndStatusForOwner(final long userId, final BookingStatus status);

    Collection<Booking> findAllByItemUserId(final long userId);

    Optional<Booking> findPastApprovedBooking(final long userId, final long itemId);
}
