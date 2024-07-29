package ru.practicum.shareit.booking.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private static final String NAME_CLASS = Booking.class.getSimpleName();

    @Override
    @Transactional
    public Booking save(final Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void patch(final Booking booking) {
        final boolean startIsEmpty = ObjectUtils.isEmpty(booking.getStart());
        final boolean endIsEmpty = ObjectUtils.isEmpty(booking.getEnd());
        final boolean statusIsEmpty = ObjectUtils.isEmpty(booking.getStatus());

        if (startIsEmpty && endIsEmpty && statusIsEmpty) {
            throw new ConditionsNotMetException(NAME_CLASS, "start, end or status fields cannot be empty");
        }

        if (ObjectUtils.isEmpty(booking.getItem()) || ObjectUtils.isEmpty(booking.getBooker())) {
            throw new ConditionsNotMetException(NAME_CLASS, "item or booker cannot be empty");
        }

        if (booking.getItem().getId() <= 0 || booking.getBooker().getId() <= 0) {
            throw new ConditionsNotMetException(NAME_CLASS, "item or booker id must be positive");
        }

        if (!startIsEmpty && !endIsEmpty && !statusIsEmpty) {
            bookingRepository.updateStartAndEndAndStatus(
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getStatus(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (!startIsEmpty && !endIsEmpty && statusIsEmpty) {
            bookingRepository.updateStartAndEnd(
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (!startIsEmpty && endIsEmpty && !statusIsEmpty) {
            bookingRepository.updateStartAndStatus(
                    booking.getStart(),
                    booking.getStatus(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (startIsEmpty && !endIsEmpty && !statusIsEmpty) {
            bookingRepository.updateEndAndStatus(
                    booking.getEnd(),
                    booking.getStatus(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (!startIsEmpty && endIsEmpty && statusIsEmpty) {
            bookingRepository.updateStart(
                    booking.getStart(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (startIsEmpty && !endIsEmpty && statusIsEmpty) {
            bookingRepository.updateEnd(
                    booking.getEnd(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }

        if (startIsEmpty && endIsEmpty && !statusIsEmpty) {
            bookingRepository.updateStatus(
                    booking.getStatus(),
                    booking.getId(),
                    booking.getItem().getId(),
                    booking.getBooker().getId()
            );
        }
    }

    @Override
    public Optional<Booking> findOneById(final long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Collection<Booking> findAllByBookerId(final long id) {
        return bookingRepository.findAllByBookerIdOrderByIdDesc(id);
    }

    @Override
    public Optional<Booking> findOneById(final long id, final long userId) {
        return bookingRepository.findOneByIdAndBookerIdOrItemUserId(id, userId);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Collection<Booking> findCurrentBookings(final long userId) {
        final LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderById(userId, now, now);
    }

    @Override
    public Collection<Booking> findPastBookings(final long userId) {
        return bookingRepository.findByBookerIdAndEndLessThanOrderByIdDesc(userId, LocalDateTime.now());
    }

    @Override
    public Collection<Booking> findFutureBookings(final long userId) {
        return bookingRepository.findByBookerIdAndStartGreaterThanOrderByIdDesc(userId, LocalDateTime.now());
    }

    @Override
    public Collection<Booking> findBookingsByBookerIdAndStatus(final long userId, BookingStatus status) {
        return bookingRepository.findByBookerIdAndStatusOrderByIdDesc(userId, status);
    }

    @Override
    public Collection<Booking> findCurrentForOwner(final long userId) {
        return bookingRepository.findCurrentForOwner(userId);
    }

    @Override
    public Collection<Booking> findPastForOwner(final long userId) {
        return bookingRepository.findByItemUserIdAndEndLessThanOrderByIdDesc(userId, LocalDateTime.now());
    }

    @Override
    public Optional<Booking> findPastApprovedBooking(final long userId, final long itemId) {
        return bookingRepository.findByBookerIdAndItemIdAndStatusAndEndLessThanEqualOrderByEndDesc(userId,
                itemId, BookingStatus.APPROVED, LocalDateTime.now(), Limit.of(1));
    }

    @Override
    public Collection<Booking> findFutureForOwner(final long userId) {
        return bookingRepository.findByItemUserIdAndStartGreaterThanOrderByIdDesc(userId, LocalDateTime.now());
    }

    @Override
    public Collection<Booking> findByBookerIdAndStatusForOwner(final long userId, BookingStatus status) {
        return bookingRepository.findBookingsByItemUserIdAndStatusOrderByIdDesc(userId, status);
    }

    @Override
    public Collection<Booking> findAllByItemUserId(final long userId) {
        return bookingRepository.findAllByItemUserIdOrderByIdDesc(userId);
    }

    @Override
    public Booking findOneByIdOrElseThrow(final long id, final long userId) {
        return bookingRepository.findBookingByBookingIdAndBookerIdOrItemUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(NAME_CLASS,
                                String.format("id : %d and userId : %d not found", id, userId)
                        )
                );
    }

    @Override
    public Booking findOneByIdOrElseThrow(final long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NAME_CLASS, "id " + id + " not found"));
    }
}
