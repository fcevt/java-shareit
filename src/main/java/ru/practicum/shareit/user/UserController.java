package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.debug("getUser: {}", id);
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto user) {
        log.debug("createUser: {}", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@RequestBody UserDto user, @PathVariable long id) {
        log.debug("updateUser: {}, userId: {}", user, id);
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.debug("deleteUser: {}", id);
        userService.deleteUser(id);
    }
}
