package ru.practicum.shareit.request.model.dto;

import lombok.Builder;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record ItemRequestWithItemsDto(
        long id,
        String description,
        LocalDateTime created,
        Collection<ItemIdAndNameDto> items
) {
}
