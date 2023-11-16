package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse  {
    private final int code;  // Status code
    private final String error; // Bad request, Not implemented etc.
    private final String description; // Err description
    private final String path; // Uri
    private final LocalDateTime time; // Err time
}