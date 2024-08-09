package ru.practicum.shareit.item.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatchItemDto(
        long id,
        String name,
        String description,
        Boolean available
) {
}
