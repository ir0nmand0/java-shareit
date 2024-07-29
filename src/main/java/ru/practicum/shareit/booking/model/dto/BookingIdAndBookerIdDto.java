package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;

@Builder
public record BookingIdAndBookerIdDto(
        long id,
        long bookerId
) {
}