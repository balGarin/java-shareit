package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RequestMapper.class, UserMapper.class})
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    @Mapping(target = "id", expression = "java(item.getId())")
    ItemDtoWithCommentsAndBookings toItemDto(Item item, List<CommentDto> comments, BookingDtoReturn lastBooking,
                                             BookingDtoReturn nextBooking);
}
