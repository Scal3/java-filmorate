package ru.yandex.practicum.filmorate.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleError(BadRequestException e) {
        return new ErrorResponse(e.getCode(), e.getError(), e.getDescription(), e.getPath(), e.getTime());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleError(NotFoundException e) {
        return new ErrorResponse(e.getCode(), e.getError(), e.getDescription(), e.getPath(), e.getTime());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleError(DataIntegrityViolationException e) {
        return new ErrorResponse(
                409,
                e.getMessage(),
                "",
                "",
                LocalDateTime.now()
        );
    }
}
