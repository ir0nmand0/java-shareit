package ru.practicum.shareit.item.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateItemDto(
        long id,
        @Size(max = 255)
        @NotBlank(message = "Value can`t be empty or null")
        String name,
        @NotBlank(message = "Value can`t be empty or null")
        String description,
        @NotNull(message = "Value can`t be null")
        Boolean available
) {
}
