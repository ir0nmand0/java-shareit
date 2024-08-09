package ru.practicum.shareit.booking.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateBookingDto(
        @Positive
        long itemId,
        @NotNull
        @FutureOrPresent
        LocalDateTime start,
        @NotNull
        @FutureOrPresent
        LocalDateTime end
) {
}
