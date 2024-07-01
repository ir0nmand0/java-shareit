package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateUserDto(
        @Positive(message = "Value must be positive")
        long id,
        @NotBlank(message = "Value can`t be empty or null")
        @Email(message = "Value must contains character @")
        String email,
        @NotBlank(message = "Value can`t be empty or null")
        String name) {
}
