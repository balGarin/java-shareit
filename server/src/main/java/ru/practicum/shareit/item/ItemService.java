package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentsAndBookings;

import java.util.List;

public interface ItemService {
    ItemDtoWithCommentsAndBookings getItemById(Integer ownerId, Integer itemId);

    ItemDto addItem(Integer ownerId, ItemDto item);

    ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto item);

    List<ItemDtoWithCommentsAndBookings> getAllItemsByOwner(Integer ownerId);

    List<ItemDto> searchByQuery(Integer ownerId, String text);

    CommentDto addComment(Integer ownerId, Integer itemId, CommentDto comment);
}
