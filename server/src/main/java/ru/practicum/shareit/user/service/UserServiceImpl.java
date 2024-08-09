package ru.practicum.shareit.user.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

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
        User userInStorage = findByIdOrElseThrow(userId);

        checkEmailOrElseThrow(userInStorage.getEmail(), patchUserDto.email());

        final boolean nameIsEmpty = ObjectUtils.isEmpty(patchUserDto.name());
        final boolean emailIsEmpty = ObjectUtils.isEmpty(patchUserDto.email());

        if (nameIsEmpty && emailIsEmpty) {
            throw new ConditionsNotMetException("user", "name or email fields cannot be empty");
        }

        if (!nameIsEmpty && !emailIsEmpty) {
            userInStorage.setEmail(patchUserDto.email());
            userInStorage.setName(patchUserDto.name());
        } else if (!nameIsEmpty) {
            userInStorage.setName(patchUserDto.name());
        } else {
            userInStorage.setEmail(patchUserDto.email());
        }

        return cs.convert(userStorage.save(userInStorage), UserDto.class);
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

    public UserDto findOneById(final long id) {
        return cs.convert(findByIdOrElseThrow(id), UserDto.class);
    }

    public void deleteById(final long id) {
        userStorage.deleteById(id);
    }

    private void checkEmailOrElseThrow(@NotBlank final String email, @NotBlank final String newEmail) {
        if (email.equalsIgnoreCase(newEmail)) {
            return;
        }

        ifDuplicateThenThrow(newEmail);
    }

    private void ifDuplicateThenThrow(final String email) {
        Optional<User> userByEmail = userStorage.findOneByEmail(email);

        if (userByEmail.isPresent()) {
            throw new EntityDuplicateException("email", "There is already a user with this email " + email);
        }
    }

    private User findByIdOrElseThrow(final long id) {
        return userStorage.findOneById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "User not found by id " + id));
    }
}
