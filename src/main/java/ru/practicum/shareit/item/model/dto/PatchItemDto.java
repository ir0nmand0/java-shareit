package ru.practicum.shareit.item.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatchItemDto(
        long id,
        @Size(max = 255)
        String name,
        String description,
        Boolean available
) {
}
