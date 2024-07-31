package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Positive
        long id,
        @Size(max = 320)
        @NotBlank
        @Email
        String email,
        @Size(max = 255)
        @NotBlank
        String name) {
}
