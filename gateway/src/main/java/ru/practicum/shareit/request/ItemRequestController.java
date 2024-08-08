package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId) {
        return itemRequestClient.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findOneById(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                              @PathVariable @Positive final long requestId) {
        return itemRequestClient.findOneById(userId, requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) @Positive final long userId,
                                         @Valid @RequestBody final CreateItemRequestDto createItemRequestDto) {
        return itemRequestClient.create(createItemRequestDto, userId);
    }

}
