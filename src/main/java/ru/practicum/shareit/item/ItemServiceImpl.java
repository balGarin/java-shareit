package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto getItemById(Integer ownerId, Integer itemId) {
        Optional<Item> optionalItem = itemRepository.findItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (item.getOwner().equals(ownerId)) {
                return ItemMapperDto.toItemDto(item);
            } else {
                throw new IncorrectDataException("Не верный владелец вещи");
            }
        } else {
            throw new NotFoundException("Вещ с таким ID не найдена");
        }

    }

    @Override
    public ItemDto addItem(Integer ownerId, Item item) {
        if (checkExistedUser(ownerId)) {
            return ItemMapperDto.toItemDto(itemRepository.saveItem(ownerId, item));
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public ItemDto updateItem(Integer ownerId, Integer itemId, Item item) {
        if (checkExistedUser(ownerId)) {
            Optional<Item> optionalItem = itemRepository.findItemById(itemId);
            if (optionalItem.isPresent()) {
                Item outdated = optionalItem.get();
                if (outdated.getOwner().equals(ownerId)) {
                    return ItemMapperDto.toItemDto(itemRepository.updateItem(itemId, item));
                } else {
                    throw new IncorrectDataException("Не верный владелец вещи");
                }
            } else {
                throw new NotFoundException("Вещ с таким ID не найдена");
            }
        } else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemRepository.findAllItemsByOwner(ownerId).stream().
                map(ItemMapperDto::toItemDto).toList();
    }

    @Override
    public List<ItemDto> searchByQuery(Integer ownerId, String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> allItems = itemRepository.findItemsByQuery(ownerId, text);
        return allItems.stream().
                filter(Item::getAvailable).
                map(ItemMapperDto::toItemDto).toList();
    }


    private boolean checkExistedUser(Integer ownerId) {
        return userRepository.findUserById(ownerId).isPresent();
    }
}
