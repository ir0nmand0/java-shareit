package ru.practicum.shareit.request.model;

import lombok.Builder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record ItemRequestWithItems(
        long id,
        User requester,
        String description,
        LocalDateTime created,
        Collection<Item> items) {
}
