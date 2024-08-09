package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;

public class UserClient extends BaseClient {
    public UserClient(final RestTemplate rest) {
        super(rest);
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
