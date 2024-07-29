package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

import java.time.LocalDateTime;

@Builder
public record BookingDto(
        long id,
        BookerId booker,
        ItemIdAndNameDto item,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status
) {
}
