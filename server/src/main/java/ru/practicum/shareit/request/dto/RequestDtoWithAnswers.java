package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.request.Answer;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDtoWithAnswers {
    private long id;
    private String description;
    private LocalDateTime created;
    List<Answer> items;
}
