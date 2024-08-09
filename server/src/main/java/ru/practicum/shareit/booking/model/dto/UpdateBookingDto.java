package ru.practicum.shareit.booking.model.dto;

import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

public record UpdateBookingDto(
        long id,
        long itemId,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status
) {
}
