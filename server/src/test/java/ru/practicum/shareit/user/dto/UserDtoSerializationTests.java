package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class UserDtoSerializationTests {

    private JacksonTester<UserDto> userDtoJson;
    private JacksonTester<UpdateUserDto> updateUserDtoJson;
    private JacksonTester<CreateUserDto> createUserDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void userDtoSerialization() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@ya.ru")
                .build();

        JsonContent<UserDto> jsonContent = userDtoJson.write(userDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "test");
        assertThat(jsonContent).hasJsonPathValue("$.email", "test@ya.ru");
    }

    @Test
    void updateUserDtoSerialization() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "test2@ya.ru", "test2");

        JsonContent<UpdateUserDto> jsonContent = updateUserDtoJson.write(updateUserDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "test2");
        assertThat(jsonContent).hasJsonPathValue("$.email", "test@ya.ru");
    }

    @Test
    void createUserDtoSerialization() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("test3@ya.ru", "test3");

        JsonContent<CreateUserDto> jsonContent = createUserDtoJson.write(createUserDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "test3");
        assertThat(jsonContent).hasJsonPathValue("$.email", "test3@ya.ru");
    }
}
