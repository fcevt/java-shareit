package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUser(long userId) {
        return userRepository.getUser(userId);
    }

    public User createUser(UserDto user) {
        return userRepository.createUser(user);
    }

    public User updateUser(UserDto user, long userId) {
        return userRepository.updateUser(user, userId);
    }

    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}
