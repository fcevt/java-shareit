package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@AutoConfigureTestDatabase
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final User user = new User();

    @Test
    public void createAndFindItemTest() {
        user.setName("TestUser");
        user.setEmail("test@test.com");
        User user1 = userRepository.save(user);
        Item item = new Item();
        item.setOwner(user1);
        item.setName("TestItem");
        item.setDescription("TestDescription");
        item.setAvailable(true);
        Item item1 = itemService.createItem(item, user1.getId());
        ItemDtoWithBooking itemDtoWithBooking = itemService.getItemById(item1.getId());
        assertThat(itemDtoWithBooking.getId(), equalTo(item1.getId()));
        assertThat(itemDtoWithBooking.getName(), equalTo(item.getName()));
        assertThat(itemDtoWithBooking.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDtoWithBooking.getAvailable(), equalTo(item.getAvailable()));
        List<ItemDtoWithBooking> userItems = itemService.getUserItems(user1.getId());
        assertThat(userItems.size(), equalTo(1));
    }

    @Test
    public void updateSearchAndCommentTest() {
        user.setName("TestUser");
        user.setEmail("test@test.com");
        User user1 = userRepository.save(user);
        Item item = new Item();
        item.setOwner(user1);
        item.setName("TestItem");
        item.setDescription("TestDescription");
        item.setAvailable(true);
        Item item1 = itemService.createItem(item, user1.getId());
        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setDescription("updated description");
        itemService.updateItem(updateDto, user1.getId(), item1.getId());
        ItemDtoWithBooking itemDtoWithBooking = itemService.getItemById(item1.getId());
        assertThat(itemDtoWithBooking.getId(), equalTo(item1.getId()));
        assertThat(itemDtoWithBooking.getName(), equalTo(item1.getName()));
        assertThat(itemDtoWithBooking.getDescription(), equalTo(updateDto.getDescription()));
        List<ItemDto> list = itemService.itemSearch(updateDto.getDescription());
        assertThat(list.size(), equalTo(1));
        ItemDto itemDto = list.get(0);
        assertThat(itemDto.getId(), equalTo(itemDtoWithBooking.getId()));
        assertThat(itemDto.getName(), equalTo(itemDtoWithBooking.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemDtoWithBooking.getDescription()));
        user.setName("TestUser2");
        user.setEmail("test2@test.com");
        User user2 = userRepository.save(user);
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item1);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().minusDays(1).plusHours(1));
        booking.setBookingStatus(StatusBooking.WAITING);
        bookingRepository.save(booking);
        Comment comment = new Comment();
        comment.setAuthorName(user2.getName());
        comment.setItem(item1);
        comment.setText("good comment");
        Comment comment1 = itemService.addComment(item1.getId(), comment, user2.getId());
        assertThat(comment1.getText(), equalTo("good comment"));
        Comment comment2 = itemService.getItemById(item1.getId()).getComments().get(0);
        assertThat(comment2.getText(), equalTo("good comment"));
    }
}
