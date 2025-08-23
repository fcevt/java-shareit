package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private long id;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User not found");
        }
        return users.get(userId);
    }

    @Override
    public User createUser(UserDto user) {
        checkUserEmail(user);
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setId(++id);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(UserDto user, long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User not found");
        }
        checkUserEmail(user);
        User oldUser = users.get(userId);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        return oldUser;
    }

    @Override
    public void deleteUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User not found");
        }
        users.remove(userId);
    }

    private void checkUserEmail(UserDto user) {
        Optional<User> existUser = users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
        if (existUser.isPresent()) {
            throw new ValidationException("Email уже используется");
        }
    }
}
