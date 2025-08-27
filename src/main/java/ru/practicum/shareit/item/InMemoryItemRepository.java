package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private long id;
    private final Map<Long, Item> items = new HashMap<>();
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        Item newItem = new Item();
        User owner = userRepository.getUser(ownerId);
        newItem.setId(id++);
        newItem.setName(itemDto.getName());
        newItem.setDescription(itemDto.getDescription());
        newItem.setAvailable(itemDto.getAvailable());
        newItem.setOwner(owner);
        items.put(newItem.getId(), newItem);
        return ItemMapper.itemToItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, long ownerId, long itemId) {
        Item item = items.get(itemId);
        User owner = userRepository.getUser(ownerId);
        if (!owner.equals(item.getOwner())) {
            throw new ValidationException("Owner mismatch");
        }
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        return ItemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
        return ItemMapper.itemToItemDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(long ownerId) {
        User owner = userRepository.getUser(ownerId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .map(ItemMapper::itemToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> itemSearch(String search) {
        if (search == null || search.isEmpty()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(search.toLowerCase())
                        || item.getDescription().toLowerCase().contains(search.toLowerCase()))
                .filter(Item::isAvailable)
                .map(ItemMapper::itemToItemDto)
                .toList();
    }
}
