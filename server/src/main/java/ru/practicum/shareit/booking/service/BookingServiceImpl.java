package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.*;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private static final String NAME_CLASS = Booking.class.getSimpleName();

    @Override
    public BookingDto create(final CreateBookingDto createBookingDto, final long userId) {
        User user = userStorage.findOneByIdOrElseThrow(userId);
        Item item = itemStorage.findOneByIdOrElseThrow(createBookingDto.itemId());
        checkAvailableIsTrueOrElseThrow(item);
        checkOwnerInItem(userId, item);
        checkTime(createBookingDto.start(), createBookingDto.end());

        Booking booking = cs.convert(createBookingDto, Booking.class);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return cs.convert(bookingStorage.save(booking), BookingDto.class);
    }

    @Override
    public BookingDto update(final UpdateBookingDto updateBookingDto, final long userId) {
        User booker = userStorage.findOneByIdOrElseThrow(userId);
        Item item = itemStorage.findOneByIdOrElseThrow(updateBookingDto.itemId());
        checkAvailableIsTrueOrElseThrow(item);
        checkOwnerInItem(userId, item);
        checkTime(updateBookingDto.start(), updateBookingDto.end());

        Booking booking = cs.convert(updateBookingDto, Booking.class);
        booking.setBooker(booker);
        booking.setItem(item);

        return cs.convert(bookingStorage.save(booking), BookingDto.class);
    }

    @Override
    public BookingDto patch(final PatchBookingDto patchBookingDto, final long id, final long userId) {
        User booker = userStorage.findOneByIdOrElseThrow(userId);
        Item item = itemStorage.findOneByIdOrElseThrow(id);
        checkAvailableIsTrueOrElseThrow(item);
        checkOwnerInItem(userId, item);

        if (!ObjectUtils.isEmpty(patchBookingDto.start()) && !ObjectUtils.isEmpty(patchBookingDto.end())) {
            checkTime(patchBookingDto.start(), patchBookingDto.end());
        }

        Booking booking = findOneByIdOrElseThrow(id);
        booking.setBooker(booker);
        booking.setItem(item);

        return cs.convert(bookingStorage.save(booking), BookingDto.class);
    }

    @Override
    public BookingDto updateBookingStatus(final long id, final boolean available, final long userId) {
        Booking booking = findOneByIdOrElseThrow(id);
        checkAvailableIsTrueOrElseThrow(booking.getItem());

        BookingStatus status = available ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        if (booking.getStatus() == status) {
            throw new ConditionsNotMetException(NAME_CLASS, "this status: " + status + " is equal to id " + id);
        }

        if (userId != booking.getItem().getUserId()) {
            throw new ConditionsNotMetException(NAME_CLASS,
                    "this userId: " + userId + " cannot edit the entry because the tool is not his"
            );
        }

        booking.setStatus(status);

        return cs.convert(bookingStorage.save(booking), BookingDto.class);
    }

    @Override
    public BookingDto findOneById(final long id, final long userId) {
        return cs.convert(bookingStorage.findOneByIdOrElseThrow(id, userId), BookingDto.class);
    }

    public Collection<BookingDto> findAllByUserId(final long id) {
        return bookingStorage.findAllByBookerId(id).stream()
                .map(booking -> cs.convert(booking, BookingDto.class))
                .toList();
    }

    @Override
    public Collection<BookingDto> findAllByIdForOwner(final long userId, final State state) {
        Collection<Booking> bookings = switch (state) {
            case CURRENT -> bookingStorage.findCurrentForOwner(userId);
            case PAST -> bookingStorage.findPastForOwner(userId);
            case FUTURE -> bookingStorage.findFutureForOwner(userId);
            case WAITING -> bookingStorage.findByBookerIdAndStatusForOwner(userId, BookingStatus.WAITING);
            case REJECTED -> bookingStorage.findByBookerIdAndStatusForOwner(userId, BookingStatus.REJECTED);
            case ALL -> bookingStorage.findAllByItemUserId(userId);
            default -> throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        };

        if (ObjectUtils.isEmpty(bookings)) {
            throw new EntityNotFoundException(NAME_CLASS,
                    String.format("Not found with id: %d and state: %s", userId, state)
            );
        }

        return bookings.stream()
                .map(booking -> cs.convert(booking, BookingDto.class))
                .toList();
    }

    @Override
    public Collection<BookingDto> findAllById(final long userId, final State state) {
        Collection<Booking> bookings = switch (state) {
            case CURRENT -> bookingStorage.findCurrentBookings(userId);
            case PAST -> bookingStorage.findPastBookings(userId);
            case FUTURE -> bookingStorage.findFutureBookings(userId);
            case WAITING -> bookingStorage.findBookingsByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingStorage.findBookingsByBookerIdAndStatus(userId, BookingStatus.REJECTED);
            case ALL -> bookingStorage.findAllByBookerId(userId);
            default -> throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        };

        if (ObjectUtils.isEmpty(bookings)) {
            throw new EntityNotFoundException(NAME_CLASS,
                    String.format("Not found with id: %d and state: %s", userId, state)
            );
        }

        return bookings.stream()
                .map(booking -> cs.convert(booking, BookingDto.class))
                .toList();
    }

    @Override
    public BookingDto findOneById(final long id) {
        return cs.convert(findOneByIdOrElseThrow(id), BookingDto.class);
    }

    @Override
    public void deleteById(final long id) {
        userStorage.deleteById(id);
    }

    private void checkOwnerInItem(final long userId, final Item item) {
        if (item.getUserId() != userId) {
            return;
        }

        throw new EntityNotFoundException(NAME_CLASS,
                "this instrument with id : " + item.getId() + " cannot be rented, because userId: "
                        + userId + " is owner");
    }

    private void checkTime(final LocalDateTime start, final LocalDateTime end) {
        if (start.isBefore(end)) {
            return;
        }

        throw new ConditionsNotMetException(NAME_CLASS, "time cannot be equal to start or be past");
    }

    private void checkAvailableIsTrueOrElseThrow(final Item item) {
        if (item.getAvailable()) {
            return;
        }

        throw new ConditionsNotMetException(NAME_CLASS,
                "this instrument with id : " + item.getId() + " cannot be rented "
        );
    }

    private Booking findOneByIdOrElseThrow(final long id) {
        return bookingStorage.findOneById(id)
                .orElseThrow(() -> new EntityNotFoundException(NAME_CLASS, "Booking not found by id: " + id)
                );
    }
}
