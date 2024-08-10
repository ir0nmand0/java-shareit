package ru.practicum.shareit.request.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemRequestDto(
        long id,
        String description,
        LocalDateTime created
) {

}
