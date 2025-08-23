package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        return itemRepository.createItem(itemDto, ownerId);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemDto, long ownerId, long itemId) {
        return itemRepository.updateItem(itemDto, ownerId, itemId);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getUserItems(long ownerId) {
        return itemRepository.getItemsByOwnerId(ownerId);
    }

    @Override
    public List<ItemDto> itemSearch(String search) {
        return itemRepository.itemSearch(search);
    }
}
