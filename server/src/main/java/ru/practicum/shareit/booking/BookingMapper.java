package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {
    BookingDtoReturn toBookingDto(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", source = "user")
    Booking toBooking(BookingDto dto, User user, Item item);


    List<BookingDtoReturn> toBookingDto(List<Booking> bookings);


}
