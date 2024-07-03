package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        if (userId <= 0) {
            return itemService.findAll();
        }

        return itemService.findAllByUserId(userId);
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
    public ItemDto findById(@PathVariable @Positive(message = "Value must be positive") final long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam final String text) {
        return itemService.findByText(text);
    }
}
