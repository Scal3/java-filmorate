package ru.yandex.practicum.filmorate.film.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final int BASE_TOP_FILM_VALUE = 10;
    FilmStorage storage;
    UserService userService;

    public FilmService(FilmStorage storage, UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }

    public Film getOneById(int id) {
        return storage.getOneById(id);
    }

    public Film getOneByName(String name) {
        return storage.getOneByName(name);
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public void create(Film film) {
        if (getOneByName(film.getName()) != null) {
            throw new BadRequestException("Film already exists", "POST /films");
        }

        storage.create(film);
    }

    public void update(Film film) {
        if (getOneById(film.getId()) == null) {
            throw new NotFoundException("Film is not found", "PUT /films");
        }

        storage.update(film);
    }

    // TODO
    public void addLike(int filmId, int userId) {
        Film film = getOneById(filmId);

        if (film == null) {
            throw new NotFoundException("Film is not found", "path");
        }

        User user = userService.getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "path");
        }

        Set<Integer> userLikesList = film.getUserLikesList();
        userLikesList.add(user.getId());
        film.setUserLikesList(userLikesList);
        update(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getOneById(filmId);

        if (film == null) {
            throw new NotFoundException("Film is not found", "path");
        }

        User user = userService.getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "path");
        }

        Set<Integer> userLikesList = film.getUserLikesList();
        userLikesList.remove(user.getId());
        film.setUserLikesList(userLikesList);
        update(film);
    }

    public List<Film> getTopFilms(int count) {
        return getAll().stream()
                .sorted(Comparator.comparingInt(film -> film.getUserLikesList().size()))
                .limit(count > 0 ? count : BASE_TOP_FILM_VALUE)
                .collect(Collectors.toList());
    }
}
