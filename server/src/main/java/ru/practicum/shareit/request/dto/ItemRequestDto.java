package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private Instant created = Instant.now();
}
