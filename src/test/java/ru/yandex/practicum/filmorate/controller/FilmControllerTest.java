package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    public void makeController() {
        controller = new FilmController();
    }

    @Test
    public void shouldCreateFilm() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(2020, Month.APRIL, 10),
                100);

        assertEquals(film, controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWhenFieldsAreNull() {
        Film film = new Film(
                1,
                null,
                null,
                null,
                0);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWhenNameIsEmpty() {
        Film film = new Film(
                1,
                "",
                "description",
                LocalDate.of(2020, Month.APRIL, 10),
                100);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsMoteThan200Symbols() {
        String description = "1234567890".repeat(30);

        Film film = new Film(
                1,
                "name",
                description,
                LocalDate.of(2020, Month.APRIL, 10),
                100);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsOlderThan28December1985() {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(1984, Month.APRIL, 10),
                100);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(2020, Month.APRIL, 10),
                -100);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
}