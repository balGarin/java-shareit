package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentsAndBookings;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoWithCommentsAndBookings getItemById(Integer ownerId, Integer itemId);

    ItemDto addItem(Integer ownerId, Item item);

    ItemDto updateItem(Integer ownerId, Integer itemId, Item item);

    List<ItemDtoWithCommentsAndBookings> getAllItemsByOwner(Integer ownerId);

    List<ItemDto> searchByQuery(Integer ownerId, String text);

    CommentDto addComment(Integer ownerId, Integer itemId, Comment comment);
}
