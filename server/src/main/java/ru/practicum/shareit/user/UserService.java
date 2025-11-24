package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserProjection;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProjection getUser(long userId) {
        UserProjection userProjection = userRepository.findUserById(userId);
        if (userProjection == null) {
            throw new NotFoundException("User not found");
        }
        return userProjection;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, long userId) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id" + userId + "не найден"));
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        return userRepository.save(oldUser);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
