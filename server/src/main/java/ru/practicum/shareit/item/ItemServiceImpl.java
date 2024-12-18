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
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           ItemMapper itemMapper,
                           CommentMapper commentMapper,
                           CommentRepository commentRepository,
                           BookingRepository bookingRepository,
                           BookingMapper bookingMapper,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.itemRequestRepository = itemRequestRepository;
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
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос не найден"));
            Item item = itemMapper.toItem(itemDto, itemRequest);
            item.setOwner(user);
            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            Item item = itemMapper.toItem(itemDto);
            item.setRequest(null);
            item.setOwner(user);
            return itemMapper.toItemDto(itemRepository.save(item));
        }
    }

    @Override
    public ItemDto updateItem(Integer ownerId, Integer itemId, ItemDto itemDto) {
        if (userRepository.existsById(ownerId)) {
            Item outdatedItem = itemRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
            if (outdatedItem.getOwner().getId().equals(ownerId)) {
                if (itemDto.getAvailable() != null) {
                    outdatedItem.setAvailable(itemDto.getAvailable());
                }
                if (itemDto.getName() != null) {
                    outdatedItem.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null) {
                    outdatedItem.setDescription(itemDto.getDescription());
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
    public List<ItemDtoWithCommentsAndBookings> getAllItemsByOwner(Integer ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        return items.stream()
                .map(item ->
                        getItemById(ownerId, item.getId())
                ).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByQuery(Integer ownerId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> allItems = itemRepository.findItemsByQuery(text);
        return allItems.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Integer ownerId, Integer itemId, CommentDto commentDto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        Comment comment = commentMapper.toComment(commentDto, user, item);
        Booking booking = bookingRepository.findByItemIdAndStatusAndBookerId(itemId, Status.APPROVED, ownerId);
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            return commentMapper.toCommentDto(commentRepository.save(comment));

        } else {
            throw new IncorrectDataException("Вы не можете оставить комментарий на эту вещь ");
        }
    }

}
