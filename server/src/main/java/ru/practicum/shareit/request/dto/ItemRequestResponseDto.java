package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ShortItemDto;

import java.time.Instant;
import java.util.List;
@Data
public class ItemRequestResponseDto {
    private Integer id;
    private String description;
    private Instant created;
    private List<ShortItemDto> items;
}
