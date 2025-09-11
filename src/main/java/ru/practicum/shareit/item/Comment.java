package ru.practicum.shareit.item;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    @Column(name = "author")
    private String authorName;
    @Column(name = "create_date")
    LocalDateTime created;
}
