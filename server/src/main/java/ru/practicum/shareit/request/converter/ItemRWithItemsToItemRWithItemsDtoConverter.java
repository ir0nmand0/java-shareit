package ru.practicum.shareit.request.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.item.converter.ItemToItemIdAndNameDtoConverter;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRWithItemsToItemRWithItemsDtoConverter implements Converter<ItemRequestWithItems, ItemRequestWithItemsDto> {
    private final ItemToItemIdAndNameDtoConverter itemIdAndNameDtoConverter;

    @Override
    public ItemRequestWithItemsDto convert(final ItemRequestWithItems source) {
        return ItemRequestWithItemsDto.builder()
                .id(source.id())
                .description(source.description())
                .created(source.created())
                .items(ObjectUtils.isEmpty(source.items()) ? List.of() : source.items().stream()
                        .map(item -> itemIdAndNameDtoConverter.convert(item))
                        .toList()
                )
                .build();
    }
}
