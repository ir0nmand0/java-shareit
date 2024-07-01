package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.PatchUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(final CreateUserDto createUserDto);

    UserDto update(final UpdateUserDto updateUserDto);

    UserDto patch(final PatchUserDto patchUserDto, final long userId);

    Collection<UserDto> findAll();

    UserDto findById(final long id);

    void delete(final long id);
}
