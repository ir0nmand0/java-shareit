package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public ItemDto create(final CreateItemDto createItemDto, final long userId) {
        userStorage.existsByIdOrElseThrow(userId);

        Item item = cs.convert(createItemDto, Item.class);
        item.setUserId(userId);
        itemRequestStorage.findOneById(createItemDto.requestId()).ifPresent(item::setRequest);

        return cs.convert(itemStorage.save(item), ItemDto.class);
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

        if (item == null) {
            throw new EntityNotFoundException("Item", "Item with id: " + itemId + " not found");
        }

        checkCreatorOfTheItem(userId, item.getUserId());

        userStorage.existsByIdOrElseThrow(userId);

        Item newItem = cs.convert(updateItemDto, Item.class);

        if (newItem.getId() == 0) {
            newItem.setId(itemId);
        }

        itemStorage.update(newItem);
        return cs.convert(newItem, ItemDto.class);
    }

    @Override
    public ItemDto patch(final PatchItemDto patchItemDto, final long itemId, final long userId) {
        userStorage.existsByIdOrElseThrow(userId);

        Item itemInStorage = null;

        if (patchItemDto.id() <= 0) {
            itemInStorage = findItemByIdOrElseThrow(itemId);
        } else {
            itemInStorage = findItemByIdOrElseThrow(patchItemDto.id());
        }

        checkCreatorOfTheItem(userId, itemInStorage.getUserId());

        final boolean nameIsEmpty = ObjectUtils.isEmpty(patchItemDto.name());
        final boolean descriptionIsEmpty = ObjectUtils.isEmpty(patchItemDto.description());
        final boolean availableIsEmpty = ObjectUtils.isEmpty(patchItemDto.available());

        if (nameIsEmpty && descriptionIsEmpty && availableIsEmpty) {
            throw new ConditionsNotMetException("item", "name, description or availableIsEmpty fields cannot be empty");
        }

        if (!nameIsEmpty && !descriptionIsEmpty && !availableIsEmpty) {
            itemInStorage.setName(patchItemDto.name());
            itemInStorage.setDescription(patchItemDto.description());
            itemInStorage.setAvailable(patchItemDto.available());
        }

        if (!nameIsEmpty && !descriptionIsEmpty && availableIsEmpty) {
            itemInStorage.setName(patchItemDto.name());
            itemInStorage.setDescription(patchItemDto.description());
        }

        if (!nameIsEmpty && descriptionIsEmpty && !availableIsEmpty) {
            itemInStorage.setName(patchItemDto.name());
            itemInStorage.setAvailable(patchItemDto.available());
        }

        if (nameIsEmpty && !descriptionIsEmpty && !availableIsEmpty) {
            itemInStorage.setDescription(patchItemDto.description());
            itemInStorage.setAvailable(patchItemDto.available());
        }

        if (!nameIsEmpty && descriptionIsEmpty && availableIsEmpty) {
            itemInStorage.setName(patchItemDto.name());
        }

        if (nameIsEmpty && !descriptionIsEmpty && availableIsEmpty) {
            itemInStorage.setDescription(patchItemDto.description());
        }

        if (nameIsEmpty && descriptionIsEmpty && !availableIsEmpty) {
            itemInStorage.setAvailable(patchItemDto.available());
        }

        return cs.convert(itemStorage.save(itemInStorage), ItemDto.class);
    }

    @Override
    public Collection<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

    @Override
    public ItemDto findOneById(final long id) {
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
    public void deleteById(final long id) {
        itemStorage.delete(id);
    }

    private Item findItemByIdOrElseThrow(final long id) {
        return itemStorage.findOneById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item", "Item not found by id " + id));
    }

    private void checkCreatorOfTheItem(final long userId, final long correctUserId) {
        if (userId != correctUserId) {
            throw new EntityNotFoundException("Item",
                    "this userId: " + userId + " cannot edit the entry because the tool is not his"
            );
        }
    }

    private Collection<Item> findItemByTextOrElseThrow(final String text) {
        return itemStorage.findAllByText(text);
    }

    private void ifDuplicateNameThenThrow(final String name) {
        if (!itemStorage.findAllByName(name).isEmpty()) {
            throw new EntityDuplicateException("name", "There is already a item with this name " + name);
        }
    }

    private void ifDuplicateDescriptionThenThrow(final String description) {
        if (!itemStorage.findAllByDescription(description).isEmpty()) {
            throw new EntityDuplicateException("description",
                    "There is already a item with this description " + description
            );
        }
    }
}
