package ru.yandex.practicum.filmorate.genre.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public Genre getOneGenreById(@PathVariable("id") int id) {
        return service.getOneGenreById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    public List<Genre> getOneGenreById() {
        return service.getAllGenre();
    }
}
