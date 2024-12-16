package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item item;
    private Item item1;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1);
        item.setName("name");
        item.setDescription("description");
        item1 = new Item();
        item1.setId(1);
        item1.setName("new");
        item1.setDescription("new");
    }

    @Test
    void testEqualsTest() {
        assertEquals(item, item1);
    }

    @Test
    void testHashCodeTest() {
        assertEquals(item.hashCode(), item1.hashCode());
    }
}