package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
}
