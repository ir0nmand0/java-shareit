package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class ItemDtoSerializationTests {

    private JacksonTester<CreateItemDto> createItemDtoJson;
    private JacksonTester<ItemDto> itemDtoJson;
    private JacksonTester<UpdateItemDto> updateItemDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void createItemDtoSerialization() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("cordless", "cordless screwdriver", true, 1L);

        JsonContent<CreateItemDto> jsonContent = createItemDtoJson.write(createItemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "cordless");
        assertThat(jsonContent).hasJsonPathValue("$.description", "cordless screwdriver");
        assertThat(jsonContent).hasJsonPathValue("$.available", true);
        assertThat(jsonContent).hasJsonPathValue("$.requestId", 1L);
    }

    @Test
    void itemDtoSerialization() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("cordless")
                .description("cordless screwdriver")
                .available(true)
                .build();

        JsonContent<ItemDto> jsonContent = itemDtoJson.write(itemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "cordless");
        assertThat(jsonContent).hasJsonPathValue("$.description", "cordless screwdriver");
        assertThat(jsonContent).hasJsonPathValue("$.available", true);
    }

    @Test
    void updateItemDtoSerialization() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .id(1L)
                .name("Updated cordless")
                .description("Updated cordless screwdriver")
                .available(false)
                .build();

        JsonContent<UpdateItemDto> jsonContent = updateItemDtoJson.write(updateItemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "Updated cordless");
        assertThat(jsonContent).hasJsonPathValue("$.description", "Updated cordless screwdriver");
        assertThat(jsonContent).hasJsonPathValue("$.available", false);
    }
}
