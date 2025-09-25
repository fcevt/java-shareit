package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotBlank
    @Email(message = "Некорректный email")
    private String email;
}
