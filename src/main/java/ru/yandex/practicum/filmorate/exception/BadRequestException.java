package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseAppException {
    public BadRequestException(String reason, String path) {
        super(HttpStatus.BAD_REQUEST, reason, path);
    }
}
