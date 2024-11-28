package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private Comment comment;
    private Comment comment1;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1);
        comment.setText("next");
        comment.setItem(new Item());
        comment1 = new Comment();
        comment1.setItem(comment.getItem());
        comment1.setId(1);
        comment1.setText("new");

    }

    @Test
    void testEquals() {
        assertEquals(comment, comment1);
    }

    @Test
    void testHashCode() {
        assertEquals(comment.hashCode(), comment1.hashCode());
    }
}