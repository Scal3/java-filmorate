package ru.yandex.practicum.filmorate.film.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetFilmById() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        FilmDbStorage storage = new FilmDbStorage(jdbcTemplate);
        storage.create(film);

        Film savedFilm = storage.getOneById(1).get();

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testGetAllFilms() {
        Film film1 = new Film(
                1,
                "film1",
                "description1",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        Film film2 = new Film(
                2,
                "film2",
                "description2",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        FilmDbStorage storage = new FilmDbStorage(jdbcTemplate);
        storage.create(film1);
        storage.create(film2);

        List<Film> filmsList = List.of(film1, film2);
        List<Film> filmsDbList = storage.getAll();

        assertThat(filmsDbList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmsList);
    }

    @Test
    public void testCreateFilm() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        FilmDbStorage storage = new FilmDbStorage(jdbcTemplate);
        storage.create(film);

        Film createdFilm = storage.getOneById(1).get();

        assertThat(createdFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        FilmDbStorage storage = new FilmDbStorage(jdbcTemplate);
        storage.create(film);

        Film createdFilm = storage.getOneById(1).get();

        assertThat(createdFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);

        film.setDescription("updated description");
        storage.update(film);

        Film updatedFilm = storage.getOneById(1).get();

        assertThat(updatedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        FilmDbStorage storage = new FilmDbStorage(jdbcTemplate);
        storage.create(film);

        Film createdFilm = storage.getOneById(1).get();

        assertThat(createdFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);

        storage.delete(1);

        boolean deletedFilm = storage.getOneById(1).isEmpty();

        assertThat(deletedFilm)
                .isEqualTo(true);
    }

    @Test
    public void testAddFilmLike() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(user);

        Set<Integer> userLikesList = new HashSet<>();
        userLikesList.add(1);
        film.setUserLikesList(userLikesList);

        filmStorage.addLike(1, 1);
        Film likedFilm = filmStorage.getOneById(1).get();

        assertThat(likedFilm.getUserLikesList())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userLikesList);
    }

    @Test
    public void testRemoveFilmLike() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(user);

        Set<Integer> userLikesList = new HashSet<>();
        userLikesList.add(1);
        film.setUserLikesList(userLikesList);

        filmStorage.addLike(1, 1);
        Film likedFilm = filmStorage.getOneById(1).get();

        assertThat(likedFilm.getUserLikesList())
                .isNotNull()
                .isEqualTo(userLikesList);

        filmStorage.removeLike(1, 1);
        Film removedLikeFilm = filmStorage.getOneById(1).get();

        assertThat(removedLikeFilm.getUserLikesList())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new HashSet<>());
    }

    @Test
    public void testGetTopFilms() {
        Film film1 = new Film(
                1,
                "film1",
                "description1",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        Film film2 = new Film(
                2,
                "film2",
                "description2",
                LocalDate.of(1985, Month.JULY, 12),
                120,
                new Mpa(1, "G")
        );

        User user = new User(
                1,
                "user@gmail.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 7)
        );

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film1);
        filmStorage.create(film2);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(user);

        filmStorage.addLike(2, 1);

        List<Film> popularFilms = List.of(film2, film1);
        List<Film> popularDbFilms = filmStorage.getTopFilms(10);

        assertThat(popularDbFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(popularFilms);
    }
}