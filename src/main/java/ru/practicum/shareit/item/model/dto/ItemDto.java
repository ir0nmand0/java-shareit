package ru.practicum.shareit.item.model.dto;

import lombok.Builder;

@Builder
public record ItemDto(
        long id,
        String name,
        String description,
        boolean available
) {
}
