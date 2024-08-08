package ru.practicum.shareit.item.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatchItemDto(
        @Size(max = 255)
        String name,
        @Size(max = 1500)
        String description,
        Boolean available
) {
}
