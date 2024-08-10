package ru.practicum.shareit.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(final String message) {
        super(message);
    }
}
