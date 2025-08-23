package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemRepository {
    ItemDto createItem(ItemDto itemDto, long ownerId);

    ItemDto updateItem(ItemUpdateDto itemDto, long ownerId, long itemId);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsByOwnerId(long ownerId);

    List<ItemDto> itemSearch(String search);
}
