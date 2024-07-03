package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto create(final CreateItemDto createItemDto, final long userId);

    ItemDto update(final UpdateItemDto updateItemDto, final long itemId, final long userId);

    ItemDto patch(final PatchItemDto patchItemDto, final long itemId, final long userId);

    Collection<ItemDto> findAll();

    Collection<ItemDto> findAllByUserId(final long userId);

    ItemDto findById(final long id);

    Collection<ItemDto> findByText(final String text);

    void delete(final long id);
}
