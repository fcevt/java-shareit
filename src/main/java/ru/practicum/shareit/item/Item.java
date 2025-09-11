package ru.practicum.shareit.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "items")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;
    @NotBlank
    @Column(nullable = false, length = 1500)
    private String description;
    @NotNull
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne(fetch = FetchType.EAGER) // при ленивой загрузке не работает эндпоинт обновления вещи
    private User owner;
//    @OneToOne(fetch = FetchType.LAZY)
//    private ItemRequest request;
}
