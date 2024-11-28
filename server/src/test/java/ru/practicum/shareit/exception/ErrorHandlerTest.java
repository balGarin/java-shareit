package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void notFoundHandle() {
        ErrorResponse response = errorHandler.notFoundHandle(new NotFoundException("Not found"));
        assertEquals("Not found", response.getError());
    }

    @Test
    void badRequestHandle() {
        ErrorResponse response = errorHandler.badRequestHandle(new IncorrectDataException("Incorrect"));
        assertEquals("Incorrect", response.getError());
    }

    @Test
    void handleOtherExceptions() {
        ErrorResponse response = errorHandler.handleOtherExceptions(new Throwable("Error"));
        assertEquals("Error", response.getError());
    }
}