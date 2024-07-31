package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Item save(Item item);

    void update(final Item item);

    void patch(final Item item);

    Collection<Item> findAll();

    Optional<Item> findOneById(final long id);

    Collection<Item> findAllByUserId(final long userId);

    Collection<Item> findAllByName(final String name);

    Collection<Item> findAllByText(final String text);

    Collection<Item> findAllByDescription(final String description);

    void delete(final long id);

    Item findOneByIdOrElseThrow(final long id);

    boolean existsById(final long id);

    boolean existsByIdAndAvailableIsTrue(final long id);

    void existsByIdAndAvailableIsTrueOrElseThrow(long id);

    void existsByIdOrElseThrow(long id);
}
