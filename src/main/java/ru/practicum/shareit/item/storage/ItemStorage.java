package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Item save(Item item);

    void update(final Item item);

    Item patch(final Item item);

    Collection<Item> findAll();

    Optional<Item> findById(final long id);

    Collection<Item> findAllByUserId(final long userId);

    Collection<Item> findByName(final String name);

    Collection<Item> findByText(final String text);

    Collection<Item> findByDescription(final String description);

    void delete(final long id);
}
