package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User getUser(long userId);

    User createUser(UserDto user);

    User updateUser(UserDto user, long userId);

    void deleteUser(long userId);
}
