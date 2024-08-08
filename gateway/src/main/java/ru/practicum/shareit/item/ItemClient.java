package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.PatchItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findAllByUserId(final long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> create(final CreateItemDto createItemDto, final long userId) {
        return post("", userId, createItemDto);
    }

    public ResponseEntity<Object> update(final UpdateItemDto updateItemDto, final long itemId, final long userId) {
        return post(String.format("/%d", itemId), userId, updateItemDto);
    }

    public ResponseEntity<Object> patch(final PatchItemDto patchItemDto, final long itemId, final long userId) {
        return patch(String.format("/%d", itemId), userId, patchItemDto);
    }

    public ResponseEntity<Object> findOneByItemIdAndUserId(final long itemId, final long userId) {
        return get(String.format("/%d", itemId), userId);
    }

    public ResponseEntity<Object> findByText(final String text) {
        return get(String.format("/search?text=%s", text));
    }

    public ResponseEntity<Object> createComment(final CreateCommentDto createCommentDto, final long itemId,
                                                final long userId) {
        return post(String.format("/%d/comment", itemId), userId, createCommentDto);
    }
}
