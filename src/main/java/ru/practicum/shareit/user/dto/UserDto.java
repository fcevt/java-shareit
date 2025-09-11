package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private long userId;
    private String name;
    private String email;
}
