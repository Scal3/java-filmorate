package ru.yandex.practicum.filmorate.mpa.dao;

import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Optional<Mpa> getOneById(int id);
    List<Mpa> getAll();
}
