package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(Integer ownerId, Integer itemId);

    ItemDto addItem(Integer ownerId, Item item);

    ItemDto updateItem(Integer ownerId, Integer itemId, Item item);

    List<ItemDto> getAllItemsByOwner(Integer ownerId);

    List<ItemDto> searchByQuery(Integer ownerId, String text);
}
