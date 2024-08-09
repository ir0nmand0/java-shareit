package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.dto.BookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private CreateItemDto createItemDto;
    private CommentDto commentDto;
    private CreateCommentDto createCommentDto;
    private ItemWithCommentDto itemWithCommentDto;

    @BeforeEach
    public void setup() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("description")
                .available(true)
                .build();

        createItemDto = CreateItemDto.builder()
                .name("Item")
                .description("description")
                .available(true)
                .build();

        itemWithCommentDto = ItemWithCommentDto.builder()
                .name("Item")
                .id(1L)
                .description("description")
                .available(true)
                .lastBooking(BookerIdDto.builder().build())
                .nextBooking(BookerIdDto.builder().build())
                .build();

        commentDto = new CommentDto(1L, "Comment text", "Author name", LocalDateTime.now());
        createCommentDto = new CreateCommentDto("Comment text");
    }

    @Test
    void createItem() throws Exception {
        when(itemService.create(any(CreateItemDto.class), anyLong())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.id()))
                .andExpect(jsonPath("$.name").value(itemDto.name()))
                .andExpect(jsonPath("$.description").value(itemDto.description()))
                .andExpect(jsonPath("$.available").value(itemDto.available()));
    }

    @Test
    void findById() throws Exception {
        when(commentService.findOneByItemIdAndUserId(anyLong(), anyLong())).thenReturn(itemWithCommentDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemWithCommentDto.id()))
                .andExpect(jsonPath("$.name").value(itemWithCommentDto.name()))
                .andExpect(jsonPath("$.description").value(itemWithCommentDto.description()))
                .andExpect(jsonPath("$.available").value(itemWithCommentDto.available()));
    }

    @Test
    void findItemsByOwnerId() throws Exception {
        when(commentService.findAllByUserId(anyLong())).thenReturn(List.of(itemWithCommentDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemWithCommentDto.id()))
                .andExpect(jsonPath("$[0].name").value(itemWithCommentDto.name()))
                .andExpect(jsonPath("$[0].description").value(itemWithCommentDto.description()))
                .andExpect(jsonPath("$[0].available").value(itemWithCommentDto.available()));
    }

    @Test
    void createComment() throws Exception {
        when(commentService.create(any(CreateCommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.id()))
                .andExpect(jsonPath("$.text").value(commentDto.text()))
                .andExpect(jsonPath("$.authorName").value(commentDto.authorName()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }
}
