package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDto {
    private Integer id;
    private String text;
    private Instant created = Instant.now();
    private String authorName;

}
