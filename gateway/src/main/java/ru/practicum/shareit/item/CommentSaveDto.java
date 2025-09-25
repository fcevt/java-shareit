package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CommentSaveDto {
    @NotBlank
    String text;
}
