package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface RequestMapper {

    ItemRequestDto toItemRequestDto(ItemRequest request);

    @Mapping(target = "requester", source = "user")
    @Mapping(target = "id", source = "itemRequestDto.id")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user);

    @Mapping(target = "id", source = "itemRequest.id")
    @Mapping(target = "description", source = "itemRequest.description")
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<Item> items);

    List<ItemRequestDto> toItemRequestDto(List<ItemRequest> list);
}
