package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select new ru.practicum.shareit.item.dto.ItemDto(it.id, it.name, it.description, it.available) " +
            "from Item as it " +
            "where (lower(it.name) like lower(?1) or lower(it.description) like lower(?1)) and it.available = true")
    List<ItemDto> searchItemByNameAndDescriptionContaining(String text);

    List<Item> findAllByOwner(User owner);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIdIn(Collection<Long> requestIds);
}
