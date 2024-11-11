package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;

@Mapper(componentModel = "spring", uses = RequestMapper.class)
public interface ItemMapper {
    ItemDto toItemDto(Item item);
}
