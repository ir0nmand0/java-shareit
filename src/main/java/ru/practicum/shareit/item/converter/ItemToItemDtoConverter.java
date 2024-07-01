package ru.practicum.shareit.item.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {
    @Override
    public ItemDto convert(final Item src) {
        return ItemDto.builder()
                .id(src.getId())
                .name(src.getName())
                .description(src.getDescription())
                .available(src.getAvailable())
                .build();
    }
}
