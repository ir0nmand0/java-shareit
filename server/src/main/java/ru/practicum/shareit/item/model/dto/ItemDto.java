package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;

@Builder
public record ItemDto(
        long id,
        String name,
        String description,
        boolean available,
        ItemRequestDto request
) {
}
