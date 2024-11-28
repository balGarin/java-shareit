package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class UserDtoTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldCorrectSerializationAndDeserializationUserDto() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("Dmitrii");
        user.setEmail("mail@gmail.com");
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        String json = mapper.writeValueAsString(userDto);
        UserDto deserializedDto = mapper.readValue(json, UserDto.class);

        assertEquals(userDto.getName(), deserializedDto.getName());
        assertEquals(userDto.getEmail(), deserializedDto.getEmail());
    }

}