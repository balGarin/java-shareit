package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           ItemMapper itemMapper, CommentRepository commentRepository,
                           CommentMapper commentMapper, BookingRepository bookingRepository,
                           BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ItemDtoWithCommentsAndBookings getItemById(Integer ownerId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
        List<CommentDto> comments = commentMapper.toCommentDto(commentRepository.findAllCommentsByItemId(itemId));
        if (ownerId.equals(item.getOwner().getId())) {
            BookingDtoReturn lastBooking = bookingMapper
                    .toBookingDto(bookingRepository.findByItemIdAndEndIsBefore(itemId, LocalDateTime.now()));
            BookingDtoReturn nextBooking = bookingMapper
                    .toBookingDto(bookingRepository.findByItemIdAndStartIsAfter(itemId, LocalDateTime.now()));
            return itemMapper.toItemDto(item, comments, lastBooking, nextBooking);
        }
        return itemMapper.toItemDto(item, comments, null, null);

    }

    @Override
    public ItemDto addItem(Integer ownerId, Item item) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        item.setOwner(user);
        return itemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto updateItem(Integer ownerId, Integer itemId, Item item) {
        if (userRepository.existsById(ownerId)) {
            Item outdatedItem = itemRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
            if (outdatedItem.getOwner().getId().equals(ownerId)) {
                if (item.getAvailable() != null) {
                    outdatedItem.setAvailable(item.getAvailable());
                }
                if (item.getName() != null) {
                    outdatedItem.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    outdatedItem.setDescription(item.getDescription());
                }
                return itemMapper.toItemDto(itemRepository.save(outdatedItem));
            } else {
                throw new IncorrectDataException("Не верный владелец");
            }
        } else {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemRepository.findAllByOwnerId(ownerId).stream()
                .map(itemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> searchByQuery(Integer ownerId, String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> allItems = itemRepository.findItemsByQuery(text);
        return allItems.stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public CommentDto addComment(Integer ownerId, Integer itemId, Comment comment) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        comment.setItem(item);
        comment.setAuthor(user);
        Booking booking = bookingRepository.findByItemIdAndStatusAndBookerId(itemId, Status.APPROVED, ownerId);
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            return commentMapper.toCommentDto(commentRepository.save(comment));

        } else {
            throw new IncorrectDataException("Вы не можете оставить комментарий на эту вещь ");
        }
    }
}
