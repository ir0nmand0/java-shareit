package ru.practicum.shareit.item.model.dto;

import lombok.Builder;

@Builder
public record ItemIdAndNameDto(
        long id,
        String name
) {
}
