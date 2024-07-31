package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Size;

public record PatchUserDto(
        @Size(max = 320)
        String email,
        @Size(max = 255)
        String name) {
}
