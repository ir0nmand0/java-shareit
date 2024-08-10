package ru.practicum.shareit.request.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findByRequesterId(final long userId);

    Optional<ItemRequest> findByRequesterIdAndId(final long userId, final long requestId);
}
