package com.example.filmorate.controller;

import com.example.filmorate.exception.ValidationException;
import com.example.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RequestMapping("/film")
@RestController
public class FilmController {
    @GetMapping
    public List<Film> getFilms() {
        return List.of(new Film());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilmModel(film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilmModel(film);

        return film;
    }

    private void validateFilmModel(Film film) throws ValidationException {
        if (film.getDuration() == null
                || film.getName() == null
                || film.getDescription() == null
                || film.getReleaseDate() == null
        ) {
            throw new ValidationException("Some field is null");
        }

        if (film.getName().isEmpty()) {
            throw new ValidationException("Film name is empty");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description is too long");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1985, Month.DECEMBER, 28))) {
            throw new ValidationException("Film release date is too old");
        }

        if (film.getDuration().isNegative()) {
            throw new ValidationException("Film duration is negative");
        }
    }
}
