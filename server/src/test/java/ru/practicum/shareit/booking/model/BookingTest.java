package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private Booking booking;
    private Booking booking1;


    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking1 = new Booking();
        booking1.setId(1);
        booking1.setStart(LocalDateTime.now().plusDays(3));
        booking1.setEnd(LocalDateTime.now().plusDays(14));

    }

    @Test
    void testEquals() {
        assertEquals(booking, booking1);
    }

    @Test
    void testHashCode() {
        assertEquals(booking.hashCode(), booking1.hashCode());
    }
}