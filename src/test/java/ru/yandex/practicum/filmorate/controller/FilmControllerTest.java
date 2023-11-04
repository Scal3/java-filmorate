package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public void shouldNotCreateFilmWhenReleaseDateIsOlderThan28December1895() {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(1894, Month.APRIL, 10),
                100);

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
}