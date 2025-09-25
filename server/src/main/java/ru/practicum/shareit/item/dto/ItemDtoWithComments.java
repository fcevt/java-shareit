package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Data
public class ItemDtoWithComments {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private List<Comment> comments;
}
