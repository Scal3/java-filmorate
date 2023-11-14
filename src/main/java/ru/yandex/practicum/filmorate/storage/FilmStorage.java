package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getOne(int id);
    List<Film> getAll();
    Film create(Film film);
    Film update(Film film);
    void delete(int id);
}
