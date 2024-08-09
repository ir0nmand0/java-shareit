package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.util.Collection;

public interface ItemRequestService {
    Collection<ItemRequestDto> findAllByUserId(final long userId);

    ItemRequestWithItemsDto findOneById(final long userId, final long requestId);

    ItemRequestDto findOneById(final long id);

    ItemRequestDto create(final CreateItemRequestDto createItemRequestDto, final long userId);
}
