package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityDuplicateException extends ResponseStatusException {
    public EntityDuplicateException(final String nameClass, final String error) {
        super(HttpStatus.CONFLICT, String.format(": %s : %s", nameClass, error));
    }
}
