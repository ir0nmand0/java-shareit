package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemRequestDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findOneById(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                               @PathVariable @Positive final long requestId) {
        return itemRequestService.findOneById(userId, requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                 @RequestBody final CreateItemRequestDto createItemRequestDto) {
        return itemRequestService.create(createItemRequestDto, userId);
    }

}
