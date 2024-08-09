package ru.practicum.shareit.item.model.dto;

import lombok.Builder;

@Builder
public record UpdateItemDto(
        long id,
        String name,
        String description,
        Boolean available,
        long requestId
) {
}
