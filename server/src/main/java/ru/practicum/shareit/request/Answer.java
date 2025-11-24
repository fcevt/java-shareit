package ru.practicum.shareit.request;

import lombok.Data;

@Data
public class Answer {
    private long itemId;
    private String name;
    private long ownerId;
}
