package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "имя не может быть пустым")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;
}
