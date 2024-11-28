package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private User user1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("name");
        user.setEmail("email");
        user1 = new User();
        user1.setId(1);
        user1.setName("nameNew");
        user1.setEmail("emailNew");
    }

    @Test
    void testEquals() {
        assertEquals(user, user1);

    }

    @Test
    void testHashCode() {

        assertEquals(user1.hashCode(), user.hashCode());
    }
}