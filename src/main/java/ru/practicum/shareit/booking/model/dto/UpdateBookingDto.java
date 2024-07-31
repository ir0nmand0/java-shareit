package ru.practicum.shareit.booking.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

public record UpdateBookingDto(
        @Positive
        long id,
        @Positive
        long itemId,
        @NotNull
        @FutureOrPresent
        LocalDateTime start,
        @NotNull
        @FutureOrPresent
        LocalDateTime end,
        @NotNull
        BookingStatus status
) {
}
