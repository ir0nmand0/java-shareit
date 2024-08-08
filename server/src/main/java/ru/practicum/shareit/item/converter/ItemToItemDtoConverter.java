package ru.practicum.shareit.item.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.request.converter.ItemRequestToItemRequestDtoConverter;

@Component
@RequiredArgsConstructor
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {
    private final ItemRequestToItemRequestDtoConverter itemRequestToItemRequestDtoConverter;

    @Override
    public ItemDto convert(final Item src) {
        return ItemDto.builder()
                .id(src.getId())
                .name(src.getName())
                .description(src.getDescription())
                .available(src.getAvailable())
                .request(ObjectUtils.isEmpty(src.getRequest()) ? null :
                        itemRequestToItemRequestDtoConverter.convert(src.getRequest())
                )
                .build();
    }
}
