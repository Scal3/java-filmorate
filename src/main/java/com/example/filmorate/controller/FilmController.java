package com.example.filmorate.controller;

import com.example.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

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
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return film;
    }
}
