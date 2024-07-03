package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(final CreateItemDto createItemDto, final long userId) {
        findUserByIdOrElseThrow(userId);

        Item item = cs.convert(createItemDto, Item.class);
        item.setUserId(userId);
        itemStorage.save(item);

        return cs.convert(item, ItemDto.class);
    }

    @Override
    public Collection<ItemDto> findAllByUserId(final long userId) {
        return itemStorage.findAllByUserId(userId).stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

    @Override
    public ItemDto update(final UpdateItemDto updateItemDto, final long itemId, final long userId) {
        Item item = null;

        if (updateItemDto.id() <= 0) {
            item = findItemByIdOrElseThrow(itemId);
        } else {
            item = findItemByIdOrElseThrow(updateItemDto.id());
        }

        checkCreatorOfTheItem(userId, item.getUserId());

        findUserByIdOrElseThrow(userId);

        Item newItem = cs.convert(updateItemDto, Item.class);

        if (newItem.getId() == 0) {
            newItem.setId(itemId);
        }

        itemStorage.update(newItem);
        return cs.convert(newItem, ItemDto.class);
    }

    @Override
    public ItemDto patch(final PatchItemDto patchItemDto, final long itemId, final long userId) {
        Item item = null;

        if (patchItemDto.id() <= 0) {
            item = findItemByIdOrElseThrow(itemId);
        } else {
            item = findItemByIdOrElseThrow(patchItemDto.id());
        }

        checkCreatorOfTheItem(userId, item.getUserId());

        findUserByIdOrElseThrow(userId);

        Item newItem = cs.convert(patchItemDto, Item.class);

        if (newItem.getId() == 0) {
            newItem.setId(itemId);
        }

        Item patchItem = itemStorage.patch(newItem);
        return cs.convert(patchItem, ItemDto.class);
    }

    @Override
    public Collection<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

    @Override
    public ItemDto findById(final long id) {
        return cs.convert(findItemByIdOrElseThrow(id), ItemDto.class);
    }

    @Override
    public Collection<ItemDto> findByText(final String text) {
        if (ObjectUtils.isEmpty(text)) {
            return List.of();
        }

        return findItemByTextOrElseThrow(text).stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

    @Override
    public void delete(final long id) {
        itemStorage.delete(id);
    }

    private Item findItemByIdOrElseThrow(final long id) {
        return itemStorage.findById(id).orElseThrow(() -> {
            final String message = "Item not found by id " + id;
            log.warn(message);
            throw new EntityNotFoundByIdException("Item", message);
        });
    }

    private void checkCreatorOfTheItem(final long userId, final long correctUserId) {
        if (userId != correctUserId) {
            final String message = "this userId: " + userId + " cannot edit the entry because the tool is not his";
            log.warn(message);
            throw new EntityNotFoundByIdException("Item", message);
        }
    }

    private User findUserByIdOrElseThrow(final long id) {
        return userStorage.findById(id).orElseThrow(() -> {
            final String message = "User not found by id " + id;
            log.warn(message);
            throw new EntityNotFoundByIdException("User", message);
        });
    }

    private Collection<Item> findItemByTextOrElseThrow(final String text) {
        Collection<Item> items = itemStorage.findByText(text);

        if (items.isEmpty()) {
            final String message = "Item not found by text " + text;
            log.warn(message);

            throw new EntityNotFoundByIdException("Item", message);
        }

        return items;
    }

    private void ifDuplicateNameThenThrow(final String name) {
        if (!itemStorage.findByName(name).isEmpty()) {
            final String message = "There is already a item with this name " + name;
            log.warn(message);

            throw new EntityDuplicateException("name", message);
        }
    }

    private void ifDuplicateDescriptionThenThrow(final String description) {
        if (!itemStorage.findByDescription(description).isEmpty()) {
            final String message = "There is already a item with this description " + description;
            log.warn(message);

            throw new EntityDuplicateException("description", message);
        }
    }
}
