package ru.yandex.practicum.filmorate.film.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.film.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.film.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.debug("Film {} has been created", film.getName());

        return filmService.getOneByName(film.getName());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.debug("Film {} has been updated", film.getName());

        return filmService.getOneById(film.getId());
    }
}
