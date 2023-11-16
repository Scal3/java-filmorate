package ru.yandex.practicum.filmorate.film.storage;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getOneById(int id);

    Film getOneByName(String name);

    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    void delete(int id);
}
