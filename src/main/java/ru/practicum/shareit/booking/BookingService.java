package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

public interface BookingService {

    BookingDtoReturn addNewBooking(Integer ownerId, BookingDto dto);

    BookingDtoReturn changeStatus(Integer bookingId, Integer ownerId, Boolean approved);

    BookingDtoReturn getBooking(Integer ownerId, Integer bookingId);

    List<BookingDtoReturn> getAllBookingsByUser(Integer ownerId, String state);
}

