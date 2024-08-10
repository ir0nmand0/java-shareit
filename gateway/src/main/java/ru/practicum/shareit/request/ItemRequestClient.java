package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;

public class ItemRequestClient extends BaseClient {
    public ItemRequestClient(final RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> findAllByUserId(final long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findOneById(final long userId, final long requestId) {
        return get(String.format("/%d", requestId), userId);
    }

    public ResponseEntity<Object> create(final CreateItemRequestDto createItemRequestDto, final long userId) {
        return post("", userId, createItemRequestDto);
    }
}
