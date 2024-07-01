package ru.practicum.shareit.item.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class CreateItemDtoToItemConverter implements Converter<CreateItemDto, Item> {
    @Override
    public Item convert(final CreateItemDto src) {
        return Item.builder()
                .name(src.name())
                .description(src.description())
                .available(src.available())
                .build();
    }
}
