package ru.practicum.shareit.user.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final UserStorage userStorage;

    public UserDto create(final CreateUserDto createUserDto) {
        ifDuplicateThenThrow(createUserDto.email());

        User user = cs.convert(createUserDto, User.class);

        user = userStorage.save(user);

        return cs.convert(user, UserDto.class);
    }

    public UserDto patch(final PatchUserDto patchUserDto, final long userId) {
        User user = findByIdOrElseThrow(userId);
        checkEmailOrElseThrow(user.getEmail(), patchUserDto.email());
        User newUser = cs.convert(patchUserDto, User.class);
        newUser.setId(userId);

        User patchUser = userStorage.patch(newUser);

        return cs.convert(patchUser, UserDto.class);
    }

    public UserDto update(final UpdateUserDto updateUserDto) {
        User oldUser = findByIdOrElseThrow(updateUserDto.id());

        checkEmailOrElseThrow(oldUser.getEmail(), updateUserDto.email());

        User user = cs.convert(updateUserDto, User.class);

        userStorage.update(user);

        return cs.convert(user, UserDto.class);
    }

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(user -> cs.convert(user, UserDto.class))
                .toList();
    }

    public UserDto findById(final long id) {
        return cs.convert(findByIdOrElseThrow(id), UserDto.class);
    }

    public void delete(final long id) {
        userStorage.delete(id);
    }

    private void checkEmailOrElseThrow(@NotBlank final String email, @NotBlank final String newEmail) {
        if (email.equalsIgnoreCase(newEmail)) {
            return;
        }

        ifDuplicateThenThrow(newEmail);
    }

    private void ifDuplicateThenThrow(final String email) {
        Optional<User> userByEmail = userStorage.findByEmail(email);

        if (userByEmail.isPresent()) {
            final String message = "There is already a user with this email " + email;
            log.warn(message);

            throw new EntityDuplicateException("email", message);
        }
    }

    private User findByIdOrElseThrow(final long id) {
        return userStorage.findById(id).orElseThrow(() -> {
            final String message = "User not found by id " + id;
            log.warn(message);
            throw new EntityNotFoundByIdException("User", message);
        });
    }
}
