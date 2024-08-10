package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public Collection<ItemRequestDto> findAllByUserId(final long userId) {
        userStorage.existsByIdOrElseThrow(userId);

        return itemRequestStorage.findAllByUserId(userId).stream()
                .map(itemRequest -> cs.convert(itemRequest, ItemRequestDto.class))
                .toList();
    }

    @Override
    public ItemRequestWithItemsDto findOneById(final long userId, final long requestId) {
        userStorage.existsByIdOrElseThrow(userId);
        ItemRequest itemRequest = itemRequestStorage.findOneByIdOrElseThrow(requestId);
        Collection<Item> items = itemStorage.findAllByUserId(userId);

        ItemRequestWithItems itemRequestWithItems = ItemRequestWithItems.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .items(items)
                .build();

        return cs.convert(itemRequestWithItems, ItemRequestWithItemsDto.class);
    }

    @Override
    public ItemRequestDto findOneById(final long id) {
        return cs.convert(itemRequestStorage.findOneByIdOrElseThrow(id), ItemRequestDto.class);
    }

    @Override
    public ItemRequestDto create(final CreateItemRequestDto createItemRequestDto, final long userId) {
        User user = userStorage.findOneByIdOrElseThrow(userId);

        ItemRequest itemRequest = cs.convert(createItemRequestDto, ItemRequest.class);

        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);

        return cs.convert(itemRequestStorage.save(itemRequest), ItemRequestDto.class);
    }
}
