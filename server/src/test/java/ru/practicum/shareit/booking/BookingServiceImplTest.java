package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void addNewBookingTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        final int id = userDto.getId();
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto.getId(), bookingDto);
        assertNotNull(bookingDtoReturn.getId(), "ID не верный");
        assertEquals(Status.WAITING, bookingDtoReturn.getStatus(), "Статус не верный");
        assertEquals(bookingDto.getEnd(), bookingDtoReturn.getEnd(), "Конец не совпадает");
        assertEquals(bookingDto.getStart(), bookingDtoReturn.getStart(), "Старт не совпадает");
        ItemDto itemDto2 = new ItemDto(null, "Cup", "For head", false, null);
        itemDto2 = itemService.addItem(userDto.getId(), itemDto2);
        final BookingDto bookingDto2 = getBookingDto(itemDto2.getId(), LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        assertThrows(IncorrectDataException.class, () -> bookingService.addNewBooking(id, bookingDto2));

    }

    @Test
    void changeStatusTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        BookingDtoReturn bookingReturn = bookingService.addNewBooking(userDto.getId(), bookingDto);
        bookingReturn = bookingService.changeStatus(bookingReturn.getId(), userDto.getId(), false);
        assertEquals(Status.CANCELED, bookingReturn.getStatus());
        bookingReturn = bookingService.changeStatus(bookingReturn.getId(), userDto.getId(), true);
        final int idBooking = bookingReturn.getId();
        assertEquals(Status.APPROVED, bookingReturn.getStatus());
        UserDto userDto2 = new UserDto(null, "Dmitry", "maidl@mail.ru");
        userDto2 = userService.addUser(userDto2);
        final int id = userDto2.getId();
        assertThrows(IncorrectDataException.class, () -> bookingService.changeStatus(idBooking, id, true));
    }

    @Test
    void getBookingTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        UserDto userDto2 = new UserDto(null, "Dmitry", "mailss@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto2.getId(), bookingDto);
        bookingDtoReturn = bookingService.getBooking(userDto2.getId(), bookingDtoReturn.getId());
        final int id = bookingDtoReturn.getId();
        assertNotNull(bookingDtoReturn.getId(), "ID не верный");
        assertEquals("Cup", bookingDtoReturn.getItem().getName(), "Название вещи не совпадает");
        assertThrows(IncorrectDataException.class, () -> bookingService.getBooking(1000, id));
    }

    @Test
    void getAllBookingsByUserFutureAndCurrentTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now(),
                LocalDateTime.now().plusDays(1));
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> dtoReturnListCurrent = bookingService.getAllBookingsByUser(userDto2.getId(), "CURRENT");
        assertEquals(1, dtoReturnListCurrent.size(), "Размер не совпадает");
        BookingDto bookingDto2 = getBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3));
        bookingService.addNewBooking(userDto2.getId(), bookingDto2);
        List<BookingDtoReturn> dtoReturnListFuture = bookingService.getAllBookingsByUser(userDto2.getId(), "FUTURE");
        assertEquals(1, dtoReturnListFuture.size(), "Размер не совпадает");
    }

    @Test
    void getAllBookingsByUserWaitingAndRejectedTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now(),
                LocalDateTime.now().plusDays(1));
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> dtoReturnListWaiting = bookingService.getAllBookingsByUser(userDto2.getId(), "WAITING");
        assertEquals(1, dtoReturnListWaiting.size(), "Размер не совпадает");
        bookingService.changeStatus(bookingDtoReturn.getId(), userDto.getId(), false);
        List<BookingDtoReturn> dtoReturnListRejected = bookingService.getAllBookingsByUser(userDto2.getId(), "REJECTED");
        assertEquals(1, dtoReturnListRejected.size(), "Размер не совпадает");
        assertThrows(IncorrectDataException.class, () -> bookingService.getAllBookingsByUser(-1, "papa"));
    }

    @Test
    void getAllBookingByCurrentUserPastAndWaitingTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        final int id = userDto.getId();
        assertThrows(IncorrectDataException.class, () -> bookingService.getAllBookingByCurrentUser(id, "ALL"));
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1));
        bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "PAST");
        assertEquals(1, list.size(), "Размер не совпадает");
        list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "WAITING");
        assertEquals(1, list.size(), "Размер не совпадает");
        assertEquals(Status.WAITING, list.get(0).getStatus(), "Статус не совпадает");
    }

    @Test
    void getAllBookingByCurrentUserFutureAndRejectedTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "FUTURE");
        assertEquals(1, list.size(), "Размер не совпадает");
        bookingService.changeStatus(bookingDtoReturn.getId(), userDto.getId(), false);
        list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "REJECTED");
        assertEquals(1, list.size(), "Размер не совпадает");

    }

    @Test
    void getAllBookingsByUserPastAndAllTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1));
        BookingDtoReturn bookingDtoReturn = bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> dtoReturnListPast = bookingService.getAllBookingsByUser(userDto2.getId(), "PAST");
        assertEquals(1, dtoReturnListPast.size(), "Размер не совпадает");
        List<BookingDtoReturn> dtoReturnListRejected = bookingService.getAllBookingsByUser(userDto2.getId(), "ALL");
        assertEquals(1, dtoReturnListRejected.size(), "Размер не совпадает");

    }

    @Test
    void getAllBookingByCurrentUserCurrentAndAllTest() {
        UserDto userDto = new UserDto(null, "Dmitry", "mail@mail.ru");
        userDto = userService.addUser(userDto);
        ItemDto itemDto = new ItemDto(null, "Cup", "For head", true, null);
        itemDto = itemService.addItem(userDto.getId(), itemDto);
        UserDto userDto2 = new UserDto(null, "Dmitry", "maill@mail.ru");
        userDto2 = userService.addUser(userDto2);
        BookingDto bookingDto = getBookingDto(itemDto.getId(), LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(3));
        bookingService.addNewBooking(userDto2.getId(), bookingDto);
        List<BookingDtoReturn> list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "CURRENT");
        assertEquals(1, list.size(), "Размер не совпадает");
        list = bookingService.getAllBookingByCurrentUser(userDto.getId(), "ALL");
        assertEquals(1, list.size(), "Размер не совпадает");
    }

    private BookingDto getBookingDto(Integer itemId, LocalDateTime start, LocalDateTime end) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        return bookingDto;
    }
}