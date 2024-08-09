package ru.practicum.shareit.item.model.dto;

import lombok.Builder;

@Builder
public record CreateItemDto(
        String name,
        String description,
        Boolean available,
        long requestId
) {
}
