package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setCreated(Instant.now());
        itemRequest.setDescription("old");
        itemRequest.setRequester(new User());
        itemRequest2 = new ItemRequest();
        itemRequest2.setId(1);
        itemRequest2.setRequester(itemRequest.getRequester());
        itemRequest2.setCreated(Instant.now().plusSeconds(120));
        itemRequest2.setDescription("new");
    }

    @Test
    void testEquals() {
        assertEquals(itemRequest2, itemRequest);
    }

    @Test
    void testHashCode() {
        assertEquals(itemRequest2.hashCode(), itemRequest.hashCode());
    }
}