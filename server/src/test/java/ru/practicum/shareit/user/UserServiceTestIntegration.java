package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserProjection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@AutoConfigureTestDatabase
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTestIntegration {
    private final UserService userService;


    @Test
    public void createAndFindUserTest() {
        User user = new User();
        user.setName("test");
        user.setEmail("test@test.com");
        User user2 = userService.createUser(user);
        UserProjection userProjection = userService.getUser(user2.getId());
        assertThat(userProjection.getName(), equalTo(user2.getName()));
        assertThat(userProjection.getEmail(), equalTo(user2.getEmail()));
        assertThat(userProjection.getId(), equalTo(user2.getId()));
    }

    @Test
    public void updateAndDeleteUserTest() {
        User user = new User();
        user.setName("test");
        user.setEmail("test@test.com");
        User user2 = userService.createUser(user);
        User userForUpdate = new User();
        userForUpdate.setName("update");
        userForUpdate.setEmail("update@test.com");
        User updatedUser = userService.updateUser(userForUpdate, user2.getId());
        UserProjection userProjection = userService.getUser(updatedUser.getId());
        assertThat(userProjection.getName(), equalTo(updatedUser.getName()));
        assertThat(userProjection.getEmail(), equalTo(updatedUser.getEmail()));
        assertThat(userProjection.getId(), equalTo(updatedUser.getId()));
        userService.deleteUser(user2.getId());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(user2.getId()));
    }
}
