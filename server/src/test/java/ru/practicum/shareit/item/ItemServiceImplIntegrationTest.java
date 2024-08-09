package ru.practicum.shareit.item;

import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.database.CommentRepository;
import ru.practicum.shareit.item.storage.database.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.database.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource("classpath:test-application.properties")
class ItemServiceImplIntegrationTest {
    private final ItemServiceImpl itemService;
    private final CommentServiceImpl commentService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void createItem() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        ItemDto itemDto = itemService.create(createItemDto, user.getId());

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.name()).isEqualTo("screwdriver");
        assertThat(itemDto.description()).isEqualTo("cordless screwdriver");
        assertThat(itemDto.available()).isTrue();
    }

    @Test
    void createItemIsFail() {
        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        assertThrows(EntityNotFoundException.class, () -> {
            itemService.create(createItemDto, Long.MAX_VALUE);
        });
    }

    @Test
    void updateItem() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        ItemDto itemDto = itemService.create(createItemDto, user.getId());

        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .name("Update screwdriver")
                .description("Update cordless screwdriver")
                .available(true)
                .build();

        ItemDto updatedItemDto = itemService.update(updateItemDto, itemDto.id(), user.getId());

        assertThat(updatedItemDto.name()).isEqualTo("Update screwdriver");
        assertThat(updatedItemDto.description()).isEqualTo("Update cordless screwdriver");
    }

    @Test
    void updateItemIsFail() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .name("Update screwdriver")
                .description("Update cordless screwdriver")
                .available(true)
                .build();

        assertThrows(EntityNotFoundException.class, () -> {
            itemService.update(updateItemDto, Long.MAX_VALUE, user.getId());
        });
    }

    @Test
    void getItemById() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        ItemDto itemDto = itemService.create(createItemDto, user.getId());

        ItemWithCommentDto itemWithCommentDto = commentService.findOneByItemIdAndUserId(itemDto.id(), user.getId());

        assertThat(itemWithCommentDto).isNotNull();
        assertThat(itemWithCommentDto.id()).isEqualTo(itemDto.id());
        assertThat(itemWithCommentDto.name()).isEqualTo(itemDto.name());
        assertThat(itemWithCommentDto.description()).isEqualTo(itemDto.description());
    }

    @Test
    void getAllItemsByOwnerId() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemDto createItemDto1 = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        CreateItemDto createItemDto2 = CreateItemDto.builder()
                .name("jigsaw")
                .description("cordless jigsaw")
                .available(true)
                .build();

        itemService.create(createItemDto1, user.getId());
        itemService.create(createItemDto2, user.getId());

        assertThat(itemService.findAllByUserId(user.getId()).size()).isEqualTo(2);
    }

    @Test
    void getItemsByText() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemDto createItemDto1 = CreateItemDto.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .build();

        CreateItemDto createItemDto2 = CreateItemDto.builder()
                .name("jigsaw")
                .description("cordless jigsaw")
                .available(true)
                .build();
        itemService.create(createItemDto1, user.getId());
        itemService.create(createItemDto2, user.getId());

        assertThat(itemService.findByText("screwdriver").size()).isEqualTo(1);
        assertThat(itemService.findByText("jigsaw").size()).isEqualTo(1);
    }
}
