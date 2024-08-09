package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateBookingDto(
        long itemId,
        LocalDateTime start,
        LocalDateTime end
) {
}
