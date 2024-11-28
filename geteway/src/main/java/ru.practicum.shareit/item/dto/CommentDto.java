package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentDto {
    private Integer id;
    @NotNull
    private String text;
    private Instant created = Instant.now();
    private String authorName;

}
