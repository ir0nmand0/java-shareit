package ru.practicum.shareit.item.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateItemDto(
        @Size(max = 255)
        @NotBlank
        String name,
        @Size(max = 1500)
        @NotBlank
        String description,
        @NotNull
        Boolean available,
        long requestId
) {
}