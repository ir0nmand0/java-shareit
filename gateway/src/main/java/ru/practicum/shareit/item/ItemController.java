package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return itemClient.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                         @Valid @RequestBody final CreateItemDto createItemDto) {
        return itemClient.create(createItemDto, userId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable final long itemId,
                                         @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                         @Valid @RequestBody final UpdateItemDto updateItemDto) {

        if (itemId <= 0 && updateItemDto.id() <= 0) {
            throw new ConditionsNotMetException("Item", "Id: " + itemId);
        }

        return itemClient.update(updateItemDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> patch(@PathVariable final long itemId,
                                        @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                        @Valid @RequestBody final PatchItemDto patchItemDto) {
        return itemClient.patch(patchItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findOneById(@PathVariable @Positive final long itemId,
                                              @RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return itemClient.findOneByItemIdAndUserId(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findAllByText(@RequestParam final String text) {
        return itemClient.findByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable @Positive final long itemId,
                                                @RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                                @Valid @RequestBody final CreateCommentDto createCommentDto) {
        return itemClient.createComment(createCommentDto, itemId, userId);
    }
}
