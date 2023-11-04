package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilmModel(film);

        film.setId(++filmId);
        films.put(filmId, film);

        log.debug("Film {} has been created", film.getName());

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) throw new NotFoundException("Film is not found");

        validateFilmModel(film);

        films.replace(film.getId(), film);

        log.debug("Film {} has been updated", film.getName());

        return film;
    }

    private void validateFilmModel(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Film release date is too old");

            throw new ValidationException("Film release date is too old");
        }
    }
}
