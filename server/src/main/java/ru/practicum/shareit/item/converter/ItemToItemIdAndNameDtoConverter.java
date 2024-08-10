package ru.practicum.shareit.item.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

@Component
public class ItemToItemIdAndNameDtoConverter implements Converter<Item, ItemIdAndNameDto> {
    @Override
    public ItemIdAndNameDto convert(final Item source) {
        return ItemIdAndNameDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
