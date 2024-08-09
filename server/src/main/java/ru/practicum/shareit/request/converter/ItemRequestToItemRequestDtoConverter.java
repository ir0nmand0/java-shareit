package ru.practicum.shareit.request.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;

@Component
public class ItemRequestToItemRequestDtoConverter implements Converter<ItemRequest, ItemRequestDto> {
    @Override
    public ItemRequestDto convert(final ItemRequest source) {
        return ItemRequestDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .created(source.getCreated())
                .build();
    }
}
