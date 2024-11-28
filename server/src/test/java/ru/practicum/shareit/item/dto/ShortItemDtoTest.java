package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ShortItemDtoTest {
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldCorrectSerializationAndDeserializationShortItemDto() throws Exception {
        Item item = new Item();
        item.setId(1);
        item.setRequest(null);
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(new User());
        ShortItemDto shortItemDto = new ShortItemDto();
        shortItemDto.setItemId(item.getId());
        shortItemDto.setName(item.getName());
        shortItemDto.setOwnerId(2);
        String json = mapper.writeValueAsString(shortItemDto);
        ShortItemDto desDto = mapper.readValue(json, ShortItemDto.class);
        assertEquals(shortItemDto.getItemId(), desDto.getItemId());
        assertEquals(shortItemDto.getName(), desDto.getName());
    }

}