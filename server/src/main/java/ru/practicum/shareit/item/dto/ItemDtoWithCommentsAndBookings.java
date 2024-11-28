package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoReturn;

import java.util.List;

@Data
public class ItemDtoWithCommentsAndBookings {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private BookingDtoReturn lastBooking;
    private BookingDtoReturn nextBooking;
}
