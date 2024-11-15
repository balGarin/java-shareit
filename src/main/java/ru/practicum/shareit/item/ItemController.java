package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentsAndBookings;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithCommentsAndBookings getItemById(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @PathVariable Integer itemId) {
        return itemService.getItemById(ownerId, itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody @Valid Item item) {
        return itemService.addItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @PathVariable Integer itemId,
                              @RequestBody Item item) {
        return itemService.updateItem(ownerId, itemId, item);
    }

    @GetMapping
    public List<ItemDtoWithCommentsAndBookings> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByQuery(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                       @RequestParam("text") String text) {
        return itemService.searchByQuery(ownerId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                 @PathVariable Integer itemId,
                                 @RequestBody Comment comment) {
        return itemService.addComment(ownerId, itemId, comment);
    }
}
