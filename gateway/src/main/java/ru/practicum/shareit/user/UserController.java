package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> create(@Valid @RequestBody final CreateUserDto createUserDto) {
        return userClient.create(createUserDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@Valid @RequestBody final UpdateUserDto updateUserDto) {
        return userClient.update(updateUserDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> patch(@PathVariable @Positive final long userId,
                                        @Valid @RequestBody PatchUserDto patchUserDto) {
        return userClient.patch(patchUserDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable @Positive final long userId) {
        return userClient.findOneById(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive final long userId) {
        userClient.deleteById(userId);
    }
}
