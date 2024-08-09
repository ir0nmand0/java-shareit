package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class CommentDtoSerializationTests {
    private JacksonTester<CommentDto> commentDtoJson;
    private JacksonTester<CreateCommentDto> createCommentDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void commentDtoSerialization() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Item")
                .authorName("UserName")
                .created(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .build();

        JsonContent<CommentDto> jsonContent = commentDtoJson.write(commentDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.text", "Item!");
        assertThat(jsonContent).hasJsonPathValue("$.authorName", "UserName");
        assertThat(jsonContent).hasJsonPathValue("$.created", "2024-08-08T15:15:15");
    }

    @Test
    void createCommentDtoSerialization() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto("Test");

        JsonContent<CreateCommentDto> jsonContent = createCommentDtoJson.write(createCommentDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.text", "Test");
    }
}
