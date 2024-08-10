package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConditionsNotMetException extends ResponseStatusException {
    public ConditionsNotMetException(final String nameClass, final String error) {
        super(HttpStatus.BAD_REQUEST, String.format(": %s : %s", nameClass, error));
    }
}
