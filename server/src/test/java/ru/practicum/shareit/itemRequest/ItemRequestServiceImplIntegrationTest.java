package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.storage.database.ItemRepository;
import ru.practicum.shareit.request.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.database.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.database.UserRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource("classpath:test.properties")
class ItemRequestServiceImplIntegrationTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createItemRequest() {
        User user = new User();
        user.setName("John Wick");
        user.setEmail("john.wick@ya.ru");
        userRepository.save(user);

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a screwdriver");
        ItemRequestDto itemRequestDto = itemRequestService.create(createItemRequestDto, user.getId());

        assertThat(itemRequestDto).isNotNull();
        assertThat(itemRequestDto.description()).isEqualTo("Need a screwdriver");
    }

    @Test
    void createItemRequestIsFail() {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a screwdriver");

        assertThrows(EntityNotFoundException.class, () -> {
            itemRequestService.create(createItemRequestDto, Long.MAX_VALUE);
        });
    }

    @Test
    void getRequestsByUserId() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a screwdriver");
        itemRequestService.create(createItemRequestDto, user.getId());

        Collection<ItemRequestDto> requests = itemRequestService.findAllByUserId(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.stream().findFirst().orElse(null).description()).isEqualTo("Need a screwdriver");
    }

    @Test
    void getRequestsById() {
        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a screwdriver");
        ItemRequestDto itemRequestDto = itemRequestService.create(createItemRequestDto, user.getId());

        ItemRequestDto request = itemRequestService.findOneById(itemRequestDto.id());

        assertThat(request).isNotNull();
        assertThat(request.description()).isEqualTo("Need a screwdriver");
    }
}
