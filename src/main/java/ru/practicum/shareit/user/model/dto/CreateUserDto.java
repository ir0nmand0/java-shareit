package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank(message = "Value can`t be empty or null")
        @Email(message = "Value must contains character @")
        String email,
        @NotBlank(message = "Value can`t be empty or null")
        String name) {
}
