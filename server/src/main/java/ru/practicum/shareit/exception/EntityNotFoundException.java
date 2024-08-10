package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundException extends ResponseStatusException {
    public EntityNotFoundException(final String nameClass, final String error) {
        super(HttpStatus.NOT_FOUND, String.format(": %s : %s", nameClass, error));
    }
}
