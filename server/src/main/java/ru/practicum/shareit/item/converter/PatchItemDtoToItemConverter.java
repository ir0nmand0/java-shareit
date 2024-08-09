package ru.practicum.shareit.item.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class PatchItemDtoToItemConverter implements Converter<PatchItemDto, Item> {
    @Override
    public Item convert(final PatchItemDto src) {
        return Item.builder()
                .id(src.id())
                .name(src.name())
                .description(src.description())
                .available(src.available())
                .build();
    }
}
