package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RequestMapper.class, UserMapper.class})


public interface ItemMapper {
    @Mapping(target = "requestId", expression = "java(item.getRequest()!=null?item.getRequest().getId():null)")
    ItemDto toItemDto(Item item);

    @Mapping(target = "request", source = "itemRequest")
    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "description", source = "itemDto.description")
    Item toItem(ItemDto itemDto, ItemRequest itemRequest);

    Item toItem(ItemDto itemDto);

    @Mapping(target = "id", expression = "java(item.getId())")
    ItemDtoWithCommentsAndBookings toItemDto(Item item, List<CommentDto> comments, BookingDtoReturn lastBooking,
                                             BookingDtoReturn nextBooking);

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "ownerId", expression = "java(item.getOwner().getId())")
    ShortItemDto toShortItemDto(Item item);

}
