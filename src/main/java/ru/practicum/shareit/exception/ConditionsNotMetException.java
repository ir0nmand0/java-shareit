package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConditionsNotMetException extends ResponseStatusException {
    public ConditionsNotMetException(String nameField, String value) {
        super(HttpStatus.BAD_REQUEST, "Conditions not met: [%s] [%s]".formatted(nameField, value));
    }
}
