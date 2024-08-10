package ru.practicum.shareit.request.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbItemRequestStorage implements ItemRequestStorage {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Collection<ItemRequest> findAllByUserId(final long userId) {
        return itemRequestRepository.findByRequesterId(userId);
    }

    @Override
    public Optional<ItemRequest> findOneById(final long userId, final long requestId) {
        return itemRequestRepository.findByRequesterIdAndId(userId, requestId);
    }

    @Override
    public Optional<ItemRequest> findOneById(final long id) {
        return itemRequestRepository.findById(id);
    }

    @Override
    public ItemRequest findOneByIdOrElseThrow(final long userId, final long requestId) {
        return findOneById(userId, requestId)
                .orElseThrow(() -> new EntityNotFoundException("ItemRequest",
                        String.format("id: %d with userId: %d not found", requestId, userId)));
    }

    @Override
    public ItemRequest findOneByIdOrElseThrow(final long id) {
        return findOneById(id)
                .orElseThrow(() -> new EntityNotFoundException("ItemRequest", "id not found"));
    }

    @Override
    @Transactional
    public ItemRequest save(final ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }
}
