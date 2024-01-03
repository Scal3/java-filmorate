package ru.yandex.practicum.filmorate.genre.dao;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getOneById(int id);

    List<Genre> getAll();
}
