package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestStorage {
    Collection<ItemRequest> findAllByUserId(final long userId);

    Optional<ItemRequest> findOneById(final long userId, final long requestId);

    Optional<ItemRequest> findOneById(final long id);

    ItemRequest findOneByIdOrElseThrow(final long userId, final long requestId);

    ItemRequest findOneByIdOrElseThrow(final long id);

    ItemRequest save(final ItemRequest itemRequest);
}
