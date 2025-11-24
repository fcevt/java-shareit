package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithAnswers;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@AutoConfigureTestDatabase
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    private final RequestService requestService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    public void createAndFindRequest() {
        User user = new User();
        user.setName("TestUser");
        user.setEmail("test@test.com");
        User user1 = userRepository.save(user);
        Item item = new Item();
        item.setOwner(user1);
        item.setName("TestItem");
        item.setDescription("TestDescription");
        item.setAvailable(true);
        Item item1 = itemRepository.save(item);
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("TestDescription");
        RequestDto requestDto1 = requestService.createRequest(requestDto, user1.getId());
        RequestDto requestDto2 = requestService.getAllRequests().get(0);
        assertThat(requestDto2, equalTo(requestDto1));
        RequestDtoWithAnswers requestDtoWithAnswers = requestService.getRequestsById(requestDto2.getId());
        assertThat(requestDtoWithAnswers.getId(), equalTo(requestDto2.getId()));
        RequestDtoWithAnswers requestDtoWithAnswers1 = requestService.getUserRequests(user1.getId()).get(0);
        assertThat(requestDtoWithAnswers1, equalTo(requestDtoWithAnswers));
    }
}
