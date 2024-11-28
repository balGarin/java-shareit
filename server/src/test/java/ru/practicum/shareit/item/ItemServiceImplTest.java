package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentsAndBookings;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void getItemByIdTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        ItemDtoWithCommentsAndBookings returned = itemService.getItemById(userDto.getId(), itemDto.getId());
        assertEquals(itemDto.getName(), returned.getName());
        assertEquals(itemDto.getDescription(), returned.getDescription());
        assertEquals(itemDto.getId(), returned.getId(), "Id не совпадают");
        UserDto userDto2 = new UserDto(null, "Dmitry", "maidl@mail.ru");
        userDto2 = userService.addUser(userDto2);
        returned = itemService.getItemById(userDto2.getId(), returned.getId());
        assertNull(returned.getLastBooking());
        assertNull(returned.getNextBooking());
    }

    @Test
    void addItemTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        assertNotNull(itemDto.getId(), "Id не верный");
        assertEquals("Cup", itemDto.getName());
        assertEquals("For head", itemDto.getDescription());
    }

    @Test
    void updateItemTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "mailsss@mail.ru");
        userDto2 = userService.addUser(userDto2);
        final int id = userDto2.getId();
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        final ItemDto itemDtoReturn = itemService.addItem(userDto.getId(), itemDto);
        final int itemId = itemDtoReturn.getId();
        itemDtoReturn.setAvailable(false);
        ItemDto updated = itemService.updateItem(userDto.getId(), itemDtoReturn.getId(), itemDtoReturn);
        assertEquals(itemDtoReturn.getName(), updated.getName(), "Имя не совпадет");
        assertEquals(itemDtoReturn.getDescription(), updated.getDescription(), "Описание не совпадет");
        assertEquals(itemDtoReturn.getId(), updated.getId(), "Id не совпадают");
        updated.setName(null);
        updated = itemService.updateItem(userDto.getId(), itemDtoReturn.getId(), itemDtoReturn);
        assertEquals(itemDtoReturn.getDescription(), updated.getDescription(), "Описание не совпадет");
        updated.setDescription(null);
        updated.setName("new");
        updated = itemService.updateItem(userDto.getId(), itemDtoReturn.getId(), itemDtoReturn);
        assertEquals(itemDtoReturn.getName(), updated.getName(), "Имя не совпадет");
        assertThrows(IncorrectDataException.class, () -> itemService.updateItem(id, itemId, itemDtoReturn));
        assertThrows(NotFoundException.class, () -> itemService.updateItem(-100, itemId, itemDtoReturn));
    }

    @Test
    void getAllItemsByOwnerTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto1 = getItemDto(null, "Cup", "For head", true);
        itemDto1 = itemService.addItem(userDto.getId(), itemDto1);
        ItemDto itemDto2 = getItemDto(null, "bag", "For bag", true);
        itemDto2 = itemService.addItem(userDto.getId(), itemDto2);
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto1);
        items.add(itemDto2);
        List<ItemDtoWithCommentsAndBookings> itemsReturned = itemService.getAllItemsByOwner(userDto.getId());
        assertEquals(items.size(), itemsReturned.size(), "Размер не совпадет");
    }

    @Test
    void searchByQueryTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        List<ItemDto> list = itemService.searchByQuery(userDto.getId(), "cup");
        assertEquals(1, list.size());
        assertEquals(itemDto.getName(), list.get(0).getName(), "Не правильно работает поиск");
        list = itemService.searchByQuery(userDto.getId(), "");
        assertEquals(0, list.size(), "Не правильно работает поиск");
    }

    @Test
    void addCommentTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Классная вещь");
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemDto.getId());
        bookingDto.setStart(LocalDateTime.now().minusDays(2));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
        BookingDtoReturn bookingReturn = bookingService.addNewBooking(userDto.getId(), bookingDto);
        bookingService.changeStatus(bookingReturn.getId(), userDto.getId(), true);
        CommentDto commentReturn = itemService.addComment(userDto.getId(), itemDto.getId(), commentDto);
        assertEquals(userDto.getName(), commentReturn.getAuthorName(), "Имена не совпадают");
        assertEquals("Классная вещь", commentReturn.getText(), "Текст не совпадает");
    }

    @Test
    void incorrectDataExceptionTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = getItemDto(null, "Cup", "For head", true);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Классная вещь");
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemDto.getId());
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        BookingDtoReturn bookingReturn = bookingService.addNewBooking(userDto.getId(), bookingDto);
        bookingService.changeStatus(bookingReturn.getId(), userDto.getId(), true);
        try {
            itemService.addComment(userDto.getId(), itemDto.getId(), commentDto);
        } catch (IncorrectDataException e) {
            ErrorResponse response = new ErrorResponse(e.getMessage());
            assertEquals("Вы не можете оставить комментарий на эту вещь ", response.getError());
        }
    }

    private ItemDto getItemDto(Integer id, String name, String description, Boolean available) {
        return new ItemDto(id, name, description, available, null);
    }
}