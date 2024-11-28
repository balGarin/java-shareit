package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import ru.practicum.shareit.item.model.Item;


import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private ItemMapper mapper = new ItemMapperImpl();


    @Test
    void testMapToItemDtoIfNull() {
        Item itemNull = mapper.toItem(null);
        assertNull(itemNull, "Маппер работает не верно");
        ItemDtoWithCommentsAndBookings itemDtoWithCommentsAndBookingsNull = mapper
                .toItemDto(null, null, null, null);
        assertNull(itemDtoWithCommentsAndBookingsNull, "Маппер работает не верно");
    }
}