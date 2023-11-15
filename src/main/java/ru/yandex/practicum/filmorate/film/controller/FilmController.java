package ru.yandex.practicum.filmorate.film.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.film.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.film.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getOneById(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.debug("Film {} has been created", film.getName());

        return filmService.getOneByName(film.getName());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.debug("Film {} has been updated", film.getName());

        return filmService.getOneById(film.getId());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.debug("Film with id {} now has +1 like from user with id {}", id, userId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.debug("Film with id {} now has -1 like from user with id {}", id, userId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam Optional<Integer> count) {
        return filmService.getTopFilms(count.orElse(0));
    }
}
