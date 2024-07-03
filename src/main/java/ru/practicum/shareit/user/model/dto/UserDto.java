package ru.practicum.shareit.user.model.dto;

import lombok.Builder;

@Builder
public record UserDto(
        long id,
        String email,
        String name
) {
}
