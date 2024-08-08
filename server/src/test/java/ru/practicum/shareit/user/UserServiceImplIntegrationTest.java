package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.database.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplIntegrationTest {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        CreateUserDto createUserDto = new CreateUserDto("test1@ya.ru", "test1");
        UserDto userDto = userService.create(createUserDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.name()).isEqualTo("test1");
        assertThat(userDto.email()).isEqualTo("test1@ya.ru");
    }

    @Test
    void createUserDuplicateEmailIsFail() {
        CreateUserDto createUserDto1 = new CreateUserDto("test2@ya.ru", "test2");
        userService.create(createUserDto1);

        CreateUserDto createUserDto2 = new CreateUserDto("test2@ya.ru", "test2");

        assertThrows(EntityDuplicateException.class, () -> {
            userService.create(createUserDto2);
        });
    }

    @Test
    void updateUser() {
        CreateUserDto createUserDto = new CreateUserDto("test1@ya.ru", "test1");
        UserDto userDto = userService.create(createUserDto);

        UpdateUserDto updateUserDto = new UpdateUserDto(userDto.id(), "test2@ya.ru", "test2");
        UserDto updatedUserDto = userService.update(updateUserDto);

        assertThat(updatedUserDto.name()).isEqualTo("test2");
        assertThat(updatedUserDto.email()).isEqualTo("test2@ya.ru");
    }

    @Test
    void deleteUser() {
        CreateUserDto createUserDto = new CreateUserDto("test@ya.ru", "test");
        UserDto userDto = userService.create(createUserDto);

        userService.deleteById(userDto.id());

        Optional<User> deletedUser = userRepository.findById(userDto.id());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void getUserById() {
        CreateUserDto createUserDto = new CreateUserDto("test@ya.ru", "test");
        UserDto userDto = userService.create(createUserDto);

        UserDto fetchedUserDto = userService.findOneById(userDto.id());

        assertThat(fetchedUserDto).isNotNull();
        assertThat(fetchedUserDto.id()).isEqualTo(userDto.id());
        assertThat(fetchedUserDto.name()).isEqualTo(userDto.name());
        assertThat(fetchedUserDto.email()).isEqualTo(userDto.email());
    }

    @Test
    void getAllUsers() {
        CreateUserDto createUserDto1 = new CreateUserDto("test@ya.ru", "test");
        CreateUserDto createUserDto2 = new CreateUserDto("test1@ya.ru", "test1");

        userService.create(createUserDto1);
        userService.create(createUserDto2);

        assertThat(userService.findAll()).hasSize(2);
    }

}
