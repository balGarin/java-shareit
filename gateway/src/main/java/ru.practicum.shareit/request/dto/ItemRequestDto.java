package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class ItemRequestDto {
    private Integer id;
    @NotNull
    private String description;
    private Instant created = Instant.now();
}
