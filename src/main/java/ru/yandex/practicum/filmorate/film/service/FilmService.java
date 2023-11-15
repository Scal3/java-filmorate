package ru.yandex.practicum.filmorate.film.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Film getOneById(int id) {
        Film film = storage.getOneById(id);

        if (film == null) {
            throw new NotFoundException("Film is not found", "GET /films/{id}");
        }

        return film;
    }

    public Film getOneByName(String name) {
        return storage.getOneByName(name);
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film create(Film film) {
        if (getOneByName(film.getName()) != null) {
            throw new BadRequestException("Film already exists", "POST /films");
        }

        return storage.create(film);
    }

    public Film update(Film film) {
        if (getOneById(film.getId()) == null) {
            throw new NotFoundException("Film is not found", "PUT /films");
        }

        return storage.update(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = getOneById(filmId);

        if (film == null) {
            throw new NotFoundException("Film is not found", "PUT /films/{id}/like/{userId}");
        }

        User user = userService.getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "PUT /films/{id}/like/{userId}");
        }

        film.getUserLikesList().add(user.getId());
    }

    public void removeLike(int filmId, int userId) {
        Film film = getOneById(filmId);

        if (film == null) {
            throw new NotFoundException("Film is not found", "DELETE /films/{id}/like/{userId}");
        }

        User user = userService.getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "DELETE /films/{id}/like/{userId}");
        }

        film.getUserLikesList().remove(user.getId());
    }

    public List<Film> getTopFilms(int count) {
        return getAll().stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getUserLikesList().size(), film1.getUserLikesList().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
