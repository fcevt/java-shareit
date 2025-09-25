package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@AutoConfigureTestDatabase
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    public void createFindAndApproveBooking() {
        User user = new User();
        user.setName("TestUser");
        user.setEmail("test@test.com");
        User owner = userRepository.save(user);
        User user2 = new User();
        user2.setName("Booker");
        user2.setEmail("booker@test.com");
        User booker = userRepository.save(user2);
        Item item = new Item();
        item.setOwner(owner);
        item.setName("TestItem");
        item.setDescription("TestDescription");
        item.setAvailable(true);
        Item item1 = itemRepository.save(item);
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(item1.getId());
        bookingCreateDto.setStart(LocalDateTime.now().plusHours(1));
        bookingCreateDto.setEnd(LocalDateTime.now().plusDays(1));
        BookingDto bookingDto = bookingService.createBooking(bookingCreateDto, booker.getId());
        BookingDto newBooking = bookingService.getBooking(bookingDto.getId(), booker.getId());
        assertThat(bookingDto, equalTo(newBooking));
        Booking allUserBookings = bookingService.getUserBookings("all", booker.getId()).get(0);
        assertThat(BookingDto.mapBookingToDto(allUserBookings), equalTo(bookingDto));
        Booking onerBookings = bookingService.getUserBookings("future", booker.getId()).get(0);
        assertThat(BookingDto.mapBookingToDto(onerBookings), equalTo(bookingDto));
        BookingDto bookingDtoApprove = bookingService.bookingApproval(bookingDto.getId(), owner.getId(), true);
        assertThat(bookingDtoApprove.getStatus().toString(), equalTo("APPROVED"));
    }

}
