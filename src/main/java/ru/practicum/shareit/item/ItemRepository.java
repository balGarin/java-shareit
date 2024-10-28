package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item saveItem(Integer ownerId, Item item);

    Item updateItem(Integer itemId, Item item);

    Optional<Item> findItemById(Integer itemId);

    List<Item> findAllItemsByOwner(Integer ownerId);

    List<Item> findItemsByQuery(Integer ownerId, String text);
}
