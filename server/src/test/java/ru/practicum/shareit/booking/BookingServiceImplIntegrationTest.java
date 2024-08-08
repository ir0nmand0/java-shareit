package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.database.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.database.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.database.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {
    private final BookingServiceImpl bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBooking() {

        User user = userRepository.save(User.builder()
                .name("Test1")
                .email("test1@ya.ru")
                .build()
        );

        User owner = userRepository.save(User.builder()
                .name("Test2")
                .email("test2@ya.ru")
                .build()
        );

        Item item = itemRepository.save(Item.builder()
                .name("screwdriver1")
                .description("cordless screwdriver1")
                .available(true)
                .userId(owner.getId())
                .build()
        );

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(item.getId())
                .build();

        BookingDto bookingDto = bookingService.create(createBookingDto, user.getId());

        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.item().id()).isEqualTo(item.getId());
        assertThat(bookingDto.booker().id()).isEqualTo(user.getId());
        assertThat(bookingDto.status()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void createBookingIsFail() {
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();

        assertThrows(EntityNotFoundException.class, () -> {
                    bookingService.create(createBookingDto, Long.MAX_VALUE);
                }
        );
    }

    @Test
    void updateBookingStatus() {

        User user = userRepository.save(User.builder()
                .name("Test3")
                .email("test3@ya.ru")
                .build()
        );

        User owner = userRepository.save(User.builder()
                .name("Test4")
                .email("test4@ya.ru")
                .build()
        );

        Item item = itemRepository.save(Item.builder()
                .name("screwdriver")
                .description("cordless screwdriver")
                .available(true)
                .userId(owner.getId())
                .build()
        );

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(item.getId())
                .build();
        BookingDto bookingDto = bookingService.create(createBookingDto, user.getId());

        BookingDto updatedBookingDto = bookingService.updateBookingStatus(bookingDto.id(), true, owner.getId());

        assertThat(updatedBookingDto.status()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void getBooking() {
        User user = userRepository.save(User.builder()
                .name("Test5")
                .email("test5@ya.ru")
                .build()
        );

        User owner = userRepository.save(User.builder()
                .name("Test6")
                .email("test6@ya.ru")
                .build()
        );

        Item item = itemRepository.save(Item.builder()
                .name("screwdriver3")
                .description("cordless screwdriver3")
                .available(true)
                .userId(owner.getId())
                .build()
        );

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();
        BookingDto bookingDto = bookingService.create(createBookingDto, user.getId());

        BookingDto fetchedBookingDto = bookingService.findOneById(bookingDto.id(), user.getId());

        assertThat(fetchedBookingDto).isNotNull();
        assertThat(fetchedBookingDto.id()).isEqualTo(bookingDto.id());
        assertThat(fetchedBookingDto.item().id()).isEqualTo(item.getId());
    }

    @Test
    void getBookings() {
        User user = userRepository.save(User.builder()
                .name("Test7")
                .email("test7@ya.ru")
                .build()
        );

        User owner = userRepository.save(User.builder()
                .name("Test8")
                .email("test8@ya.ru")
                .build()
        );

        Item item = itemRepository.save(Item.builder()
                .name("screwdriver4")
                .description("cordless screwdriver4")
                .available(true)
                .userId(owner.getId())
                .build()
        );


        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(item.getId())
                .build();
        bookingService.create(createBookingDto, user.getId());

        Collection<BookingDto> bookings = bookingService.findAllById(user.getId(), State.ALL);

        assertThat(bookings).hasSize(1);
    }
}
