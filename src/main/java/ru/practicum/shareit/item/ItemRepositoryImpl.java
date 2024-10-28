package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item saveItem(Integer ownerId, Item item) {
        item.setId(generateId());
        item.setOwner(ownerId);
        items.put(item.getId(), item);
        log.info("Добавлена новая вещ - {}", item);
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Integer itemId, Item item) {
        Item outdated = items.get(itemId);
        Item updatedItem = new Item(itemId,
                item.getName() != null ? item.getName() : outdated.getName(),
                item.getDescription() != null ? item.getDescription() : outdated.getDescription(),
                item.getAvailable() != null ? item.getAvailable() : outdated.getAvailable(),
                outdated.getOwner(),
                outdated.getRequest());
        log.info("Вещ - {} обновлена на - \n{}", outdated, updatedItem);
        items.put(itemId, updatedItem);
        return items.get(itemId);

    }

    @Override
    public Optional<Item> findItemById(Integer itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findAllItemsByOwner(Integer ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId)).toList();
    }

    @Override
    public List<Item> findItemsByQuery(Integer ownerId, String text) {
        return items.values().stream().peek(item -> {
                    item.setName(item.getName().toLowerCase());
                    item.setDescription(item.getDescription().toLowerCase());
                }).filter(item -> item.getOwner().equals(ownerId))
                .filter(item -> item.getName().contains(text.toLowerCase()) ||
                        item.getDescription().contains(text.toLowerCase())).toList();
    }

    private Integer generateId() {
        Integer id = items.values().stream()
                .mapToInt(Item::getId).max().orElse(0);
        return ++id;
    }
}
