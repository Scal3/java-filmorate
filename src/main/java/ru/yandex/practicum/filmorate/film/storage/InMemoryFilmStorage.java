package ru.yandex.practicum.filmorate.film.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public Film getOne(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(++filmId);
        films.put(filmId, film);

        return film;
    }

    @Override
    public Film update(Film film) {
        films.replace(film.getId(), film);

        return film;
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }
}
