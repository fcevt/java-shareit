package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDtoWithBooking getItemById(@PathVariable long id) {
        log.debug("Getting item by id: {}", id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDtoWithBooking> getUserItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("Getting user items: {}", ownerId);
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestParam String text) {
        log.debug("Searching text: {}", text);
        return itemService.itemSearch(text);
    }

    @PostMapping
    public Item createItem(@RequestBody @Valid Item item, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.debug("Created new item: {}, ownerId: {},", item, ownerId);
        return itemService.createItem(item, ownerId);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestBody ItemUpdateDto itemUpdateDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long id) {
        log.debug("Updating item: {}, ownerId: {}, itemId: {}", itemUpdateDto, ownerId, id);
        return itemService.updateItem(itemUpdateDto, ownerId, id);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@PathVariable long itemId, @RequestBody Comment comment,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Adding comment: {}, itemId: {}", comment, itemId);
        return itemService.addComment(itemId, comment, userId);
    }
}