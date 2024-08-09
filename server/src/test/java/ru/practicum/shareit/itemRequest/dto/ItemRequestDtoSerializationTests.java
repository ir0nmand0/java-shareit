package ru.practicum.shareit.itemRequest.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestWithItemsDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class ItemRequestDtoSerializationTests {

    private JacksonTester<CreateItemRequestDto> createItemRequestDtoJson;
    private JacksonTester<ItemRequestDto> itemRequestDtoJson;
    private JacksonTester<ItemRequestWithItemsDto> itemRequestWithItemsDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void createItemRequestDtoSerialization() throws Exception {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("screwdriver");

        JsonContent<CreateItemRequestDto> jsonContent = createItemRequestDtoJson.write(createItemRequestDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.description", "screwdriver");
    }

    @Test
    void itemRequestDtoSerialization() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .build();

        JsonContent<ItemRequestDto> jsonContent = itemRequestDtoJson.write(itemRequestDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.description", "test");
        assertThat(jsonContent).hasJsonPathValue("$.created", "2024-08-08T15:15:15");
    }
}
