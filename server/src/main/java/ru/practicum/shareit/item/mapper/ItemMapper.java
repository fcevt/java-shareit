package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.request.Answer;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemDtoWithComments itemToItemDtoWithComments(Item item, List<Comment> comments) {
        ItemDtoWithComments itemDtoWithComments = new ItemDtoWithComments();
        itemDtoWithComments.setId(item.getId());
        itemDtoWithComments.setName(item.getName());
        itemDtoWithComments.setDescription(item.getDescription());
        itemDtoWithComments.setAvailable(item.getAvailable());
        itemDtoWithComments.setComments(comments);
        return itemDtoWithComments;
    }

    public static ItemDtoWithBooking addBookingDateInItemDtoWithComments(
            Booking lastBooking, Booking nextBooking, ItemDtoWithComments itemDtoWithComments) {
        ItemDtoWithBooking itemDtoWithBooking = new ItemDtoWithBooking();
        itemDtoWithBooking.setId(itemDtoWithComments.getId());
        itemDtoWithBooking.setName(itemDtoWithComments.getName());
        itemDtoWithBooking.setDescription(itemDtoWithComments.getDescription());
        itemDtoWithBooking.setAvailable(itemDtoWithComments.getAvailable());
        itemDtoWithBooking.setComments(itemDtoWithComments.getComments());
        itemDtoWithBooking.setLastBooking(lastBooking);
        itemDtoWithBooking.setNextBooking(nextBooking);
        return itemDtoWithBooking;
    }

    public static Answer itemToAnswer(Item item) {
        Answer answer = new Answer();
        answer.setItemId(item.getId());
        answer.setName(item.getName());
        answer.setOwnerId(item.getOwner().getId());
        return answer;
    }
}
