package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemWithCommentDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return commentService.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                          @Valid @RequestBody final CreateItemDto createItemDto) {
        return itemService.create(createItemDto, userId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable final long itemId,
                          @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                          @Valid @RequestBody final UpdateItemDto updateItemDto) {

        if (itemId <= 0 && updateItemDto.id() <= 0) {
            throw new ConditionsNotMetException("Item", "Id: " + itemId);
        }

        return itemService.update(updateItemDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto patch(@PathVariable final long itemId,
                         @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                         @Valid @RequestBody final PatchItemDto patchItemDto) {
        return itemService.patch(patchItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentDto findOneById(@PathVariable @Positive final long itemId,
                                          @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return commentService.findOneByItemIdAndUserId(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findAllByText(@RequestParam final String text) {
        return itemService.findByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable @Positive final long itemId,
                                    @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                    @Valid @RequestBody final CreateCommentDto createCommentDto) {
        return commentService.create(createCommentDto, itemId, userId);
    }
}
