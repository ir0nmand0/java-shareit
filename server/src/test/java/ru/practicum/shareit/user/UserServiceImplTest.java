package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.database.InDbUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private InDbUserStorage userStorage;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        CreateUserDto createUserDto = new CreateUserDto("test@ya.ru", "test");
        User user = new User(1L, "test@ya.ru", "test");
        UserDto userDto = new UserDto(1L, "test@ya.ru", "test");

        when(conversionService.convert(createUserDto, User.class)).thenReturn(user);
        when(userStorage.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto createdUser = userService.create(createUserDto);

        assertNotNull(createdUser);
        assertEquals("test@ya.ru", createdUser.email());
        assertEquals("test", createdUser.name());
        verify(userStorage, times(1)).save(user);
    }

    @Test
    void createUserIsFail() {
        CreateUserDto createUserDto = new CreateUserDto("test@ya.ru", "test");
        when(userStorage.findOneByEmail(createUserDto.email())).thenReturn(Optional.of(new User()));
        when(conversionService.convert(createUserDto, User.class))
                .thenReturn(User.builder()
                        .id(1L)
                        .email(createUserDto.email())
                        .name(createUserDto.name())
                        .build());

        assertThrows(EntityDuplicateException.class, () -> userService.create(createUserDto));
        verify(userStorage, never()).save(any(User.class));
    }

    @Test
    void deleteUser() {
        long userId = 1L;
        when(userStorage.findOneById(userId)).thenReturn(Optional.of(new User()));

        userService.deleteById(userId);

        verify(userStorage, times(1)).deleteById(userId);
    }

    @Test
    void getUserById() {
        long userId = 1L;
        User user = new User(userId, "test@ya.ru", "test");
        UserDto userDto = new UserDto(userId, "test@ya.ru", "test");

        when(userStorage.findOneById(userId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.findOneById(userId);

        assertNotNull(result);
        assertEquals("test", result.name());
        assertEquals("test@ya.ru", result.email());
    }

    @Test
    void getUserByIdIsFail() {
        long userId = 1L;
        when(userStorage.findOneById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findOneById(userId));
    }

    @Test
    void getAllUsers() {
        User user = new User(1L, "test@ya.ru", "test");
        UserDto userDto = new UserDto(1L, "test@ya.ru", "test");

        when(userStorage.findAll()).thenReturn(List.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        Collection<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("test", result.stream().findFirst().get().name());
    }
}
