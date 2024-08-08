package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> findOneById(final long userId, final long requestId) {
        return get(String.format("/%d", requestId), userId);
    }

    public ResponseEntity<Object> create(final CreateItemRequestDto createItemRequestDto, final long userId) {
        return post("", userId, createItemRequestDto);
    }
}
