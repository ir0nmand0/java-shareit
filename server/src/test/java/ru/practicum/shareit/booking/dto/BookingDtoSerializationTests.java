package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.dto.BookerIdDto;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class BookingDtoSerializationTests {

    private JacksonTester<BookingDto> bookingDtoJson;
    private JacksonTester<CreateBookingDto> createBookingDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void bookingDtoSerialization() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .end(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .item(ItemIdAndNameDto.builder().id(1L).name("Item").build())
                .booker(BookerIdDto.builder().id(1L).build())
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> jsonContent = bookingDtoJson.write(bookingDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.start", "2024-08-08T15:15:15");
        assertThat(jsonContent).hasJsonPathValue("$.end", "2024-08-08T15:15:15");
        assertThat(jsonContent).hasJsonPathValue("$.item.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.item.name", "Item");
        assertThat(jsonContent).hasJsonPathValue("$.booker.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.status", "APPROVED");
    }

    @Test
    void createBookingDtoSerialization() throws Exception {
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .end(LocalDateTime.of(2024, 8, 8, 15, 15, 15))
                .build();

        JsonContent<CreateBookingDto> jsonContent = createBookingDtoJson.write(createBookingDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.itemId", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.start", "2024-08-08T15:15:15");
        assertThat(jsonContent).hasJsonPathValue("$.end", "2024-08-08T15:15:15");
    }

}
