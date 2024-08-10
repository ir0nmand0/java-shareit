package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final String ERROR = "error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedStatus(final UnsupportedStatusException e) {
        log.warn(e.getMessage(), HttpStatus.BAD_REQUEST);
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFound(final EntityNotFoundException e) {
        log.warn(e.getMessage(), HttpStatus.NOT_FOUND);
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEntityDuplicate(final EntityDuplicateException e) {
        log.warn(e.getMessage(), HttpStatus.CONFLICT);
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServer(final InternalServerException e) {
        log.warn(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return Map.of(ERROR, e.getMessage());
    }
}
