package ru.practicum.shareit.user.model.dto;

public record UpdateUserDto(
        long id,
        String email,
        String name) {
}
