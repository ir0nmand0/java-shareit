package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final String ERROR_MESSAGE =  "Value must be positive";

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody final CreateUserDto createUserDto) {
        return userService.create(createUserDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@Valid @RequestBody final UpdateUserDto updateUserDto) {
        return userService.update(updateUserDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto patch(@PathVariable @Positive(message = ERROR_MESSAGE) final long userId,
                         @Valid @RequestBody PatchUserDto patchUserDto) {
        return userService.patch(patchUserDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable @Positive(message = ERROR_MESSAGE) final long userId) {
        return userService.findOneById(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive(message = ERROR_MESSAGE) final long userId) {
        userService.deleteById(userId);
    }
}
