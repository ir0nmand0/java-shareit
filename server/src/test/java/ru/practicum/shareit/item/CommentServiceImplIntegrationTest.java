package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.database.BookingRepository;
import ru.practicum.shareit.booking.storage.database.InDbBookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.storage.database.CommentRepository;
import ru.practicum.shareit.item.storage.database.InDbCommentStorage;
import ru.practicum.shareit.item.storage.database.InDbItemStorage;
import ru.practicum.shareit.item.storage.database.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.database.InDbUserStorage;
import ru.practicum.shareit.user.storage.database.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentServiceImplIntegrationTest {
    private final CommentServiceImpl commentService;
    private final InDbCommentStorage commentStorage;
    private final InDbBookingStorage bookingStorage;
    private final InDbUserStorage userStorage;
    private final InDbItemStorage itemStorage;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createComment() {
        User user = userStorage.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        User owner = userStorage.save(User.builder()
                .name("Test2")
                .email("test2@ya.ru")
                .build()
        );

        Item item = itemStorage.save(Item.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .userId(owner.getId())
                .build()
        );

        bookingRepository.save(Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build()
        );

        CreateCommentDto createCommentDto = new CreateCommentDto("Test Comment");
        CommentDto commentDto = commentService.create(createCommentDto, item.getId(), user.getId());

        assertThat(commentDto).isNotNull();
        assertThat(commentDto.text()).isEqualTo("Test Comment");
        assertThat(commentDto.authorName()).isEqualTo(user.getName());
    }

    @Test
    void createCommentIsFail() {
        User user = userStorage.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        User owner = userStorage.save(User.builder()
                .name("Test2")
                .email("test2@ya.ru")
                .build()
        );

        Item item = itemStorage.save(Item.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .userId(owner.getId())
                .build()
        );

        CreateCommentDto createCommentDto = new CreateCommentDto("Test Comment");

        assertThrows(ResponseStatusException.class, () -> {
            commentService.create(createCommentDto, item.getId(), user.getId());
        });
    }
}
