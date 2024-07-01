package ru.practicum.shareit.item.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateItemDto(
        @NotBlank(message = "Value can`t be empty or null")
        String name,
        @NotBlank(message = "Value can`t be empty or null")
        String description,
        @NotNull(message = "Value can`t be null")
        Boolean available
) {
}
