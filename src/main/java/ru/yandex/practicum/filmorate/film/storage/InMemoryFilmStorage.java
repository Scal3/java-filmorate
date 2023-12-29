package ru.yandex.practicum.filmorate.film.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public Optional<Film> getOneById(int id) {
        return Optional.ofNullable(films.get(id));
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

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getTopFilms(int count) {
        return null;
    }
}
