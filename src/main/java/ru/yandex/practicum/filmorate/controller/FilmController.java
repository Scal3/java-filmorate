package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RequestMapping("/film")
@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilms() {
        return List.of(new Film());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilmModel(film);
        log.debug("Film {} has been created", film.getName());

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilmModel(film);
        log.debug("Film {} has been updated", film.getName());

        return film;
    }

    private void validateFilmModel(Film film) throws ValidationException {
        if (film.getDuration() == null
                || film.getName() == null
                || film.getDescription() == null
                || film.getReleaseDate() == null
        ) {
            log.warn("Some field is null");

            throw new ValidationException("Some field is null");
        }

        if (film.getName().isEmpty()) {
            log.warn("Film name is empty");

            throw new ValidationException("Film name is empty");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Film description is too long");

            throw new ValidationException("Film description is too long");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1985, Month.DECEMBER, 28))) {
            log.warn("Film release date is too old");

            throw new ValidationException("Film release date is too old");
        }

        if (film.getDuration().isNegative()) {
            log.warn("Film duration is negative");

            throw new ValidationException("Film duration is negative");
        }
    }
}
