package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;

@Data
public class CommentDto {
    private Integer id;
    private String text;
    private ItemDto item;
    private String authorName;
    private Instant created;
}
