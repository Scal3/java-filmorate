package ru.yandex.practicum.filmorate.film.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Film getOneById(int id) {
        return getFilmOrThrowException(id, "Film is not found", "GET /films/{id}");
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public Film update(Film film) {
        getFilmOrThrowException(film.getId(), "Film is not found", "PUT /films");

        return storage.update(film);
    }

    public void addLike(int filmId, int userId) {
        getFilmOrThrowException(filmId, "Film is not found", "PUT /films/{id}/like/{userId}");
        userService.getOneById(userId);

        storage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmOrThrowException(filmId, "Film is not found", "DELETE /films/{id}/like/{userId}");
        userService.getOneById(userId);

        storage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int count) {
        return storage.getTopFilms(count);
    }

    private Film getFilmOrThrowException(int id, String errMessage, String endpoint) {
        Optional<Film> optionalFilm = storage.getOneById(id);

        if (optionalFilm.isEmpty()) {
            throw new NotFoundException(errMessage, endpoint);
        }

        return optionalFilm.get();
    }
}
