package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id) {
        log.debug("Getting item by id: {}", id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("Getting user items: {}", ownerId);
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestParam String text) {
        log.debug("Searching text: {}", text);
        return itemService.itemSearch(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("Created new item: {}, ownerId: {},", itemDto, ownerId);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemUpdateDto itemUpdateDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long id) {
        log.debug("Updating item: {}, ownerId: {}, itemId: {}", itemUpdateDto, ownerId, id);
        return itemService.updateItem(itemUpdateDto, ownerId, id);
    }
}