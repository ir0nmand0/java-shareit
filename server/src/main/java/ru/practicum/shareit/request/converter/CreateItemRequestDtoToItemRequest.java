package ru.practicum.shareit.request.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;

@Component
public class CreateItemRequestDtoToItemRequest implements Converter<CreateItemRequestDto, ItemRequest> {
    @Override
    public ItemRequest convert(final CreateItemRequestDto source) {
        return ItemRequest.builder()
                .description(source.description())
                .build();
    }
}
