package ru.practicum.shareit.request.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateItemRequestDto(
        @Size(max = 1500)
        @NotBlank
        String description
) {
}
