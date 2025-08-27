package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long ownerId);

    ItemDto updateItem(ItemUpdateDto itemUpdateDto, long ownerId, long itemId);

    ItemDto getItemById(long id);

    List<ItemDto> getUserItems(long ownerId);

    List<ItemDto> itemSearch(String search);
}
