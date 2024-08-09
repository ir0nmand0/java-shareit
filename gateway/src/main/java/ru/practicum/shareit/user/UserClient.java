package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;

@Component
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> create(final CreateUserDto createUserDto) {
        return post("", createUserDto);
    }

    public ResponseEntity<Object> update(final UpdateUserDto updateUserDto) {
        return post("", updateUserDto);
    }

    public ResponseEntity<Object> patch(PatchUserDto patchUserDto, long userId) {
        return patch(String.format("/%d", userId), patchUserDto);
    }

    public ResponseEntity<Object> findOneById(final long userId) {
        return get(String.format("/%d", userId));
    }

    public void deleteById(final long userId) {
        delete(String.format("/%d", userId));
    }
}
