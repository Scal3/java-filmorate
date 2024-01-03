package ru.yandex.practicum.filmorate.film.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        return new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"))
        );
    };

    private final RowMapper<Film> filmGenresRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));

        Film filmWithGenre = new Film();
        filmWithGenre.setId(rs.getInt("film_id"));
        filmWithGenre.setGenres(new HashSet<>(List.of(genre)));

        return filmWithGenre;
    };

    private final RowMapper<Integer> filmUserLikesRowMapper = (rs, rowNum) -> rs.getInt("user_id");

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));

        return genre;
    };

    @Override
    public Optional<Film> getOneById(int id) {
        String sqlFilmQuery =
                            "SELECT " +
                                "film.id, " +
                                "film.film_name AS name, " +
                                "film.description, " +
                                "film.releaseDate, " +
                                "film.duration, " +
                                "mpa.id AS mpa_id, " +
                                "mpa.mpa_value AS mpa_name " +
                            "FROM film " +
                            "JOIN mpa ON film.mpa_id = mpa.id " +
                            "WHERE film.id = ?;";

        Optional<Film> optionalFilm = jdbcTemplate.query(sqlFilmQuery, filmRowMapper, id)
                .stream()
                .findFirst();

        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            String sqlGenresQuery =
                                "SELECT " +
                                    "film_genre.genre_id, " +
                                    "genre.genre_name " +
                                "FROM film_genre " +
                                "JOIN genre ON film_genre.genre_id = genre.id " +
                                "WHERE film_genre.film_id = ?;";

            film.getGenres().addAll(jdbcTemplate.query(sqlGenresQuery, genreRowMapper, film.getId()));

            String sqlFilmLikesQuery =
                                "SELECT " +
                                    "user_id " +
                                "FROM film_user_like " +
                                "WHERE film_id = ?;";

            film.getUserLikesList().addAll(
                    jdbcTemplate.query(sqlFilmLikesQuery, filmUserLikesRowMapper, film.getId())
            );

            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAll() {
        String sqlFilmsQuery =
                            "SELECT " +
                                "film.id, " +
                                "film.film_name AS name, " +
                                "film.description, " +
                                "film.releaseDate, " +
                                "film.duration, " +
                                "mpa.id AS mpa_id, " +
                                "mpa.mpa_value AS mpa_name " +
                            "FROM film " +
                            "JOIN mpa ON film.mpa_id = mpa.id;";

        List<Film> films = jdbcTemplate.query(sqlFilmsQuery, filmRowMapper);

        String sqlGenresQuery =
                                "SELECT " +
                                    "film_genre.film_id, " +
                                    "film_genre.genre_id, " +
                                    "genre.genre_name " +
                                "FROM film_genre " +
                                "JOIN genre ON film_genre.genre_id = genre.id;";

        List<Film> filmsGenres = jdbcTemplate.query(sqlGenresQuery, filmGenresRowMapper);

        for (Film g : filmsGenres) {
            for (Film f : films) {
                if (f.getId().equals(g.getId())) {
                    f.getGenres().addAll(g.getGenres());
                }
            }
        }

        return films;
    }

    @Override
    public Film create(Film film) {
        String sqlFilmInsert =
                "INSERT INTO film (film_name, description, releaseDate, duration, mpa_id) " +
                        "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlFilmInsert, new String[]{"ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        int filmId = keyHolder.getKey().intValue();

        for (Genre genre : film.getGenres()) {
            String sqlGenreInsert =
                    "INSERT INTO film_genre (film_id, genre_id) " +
                            "VALUES (?, ?);";

            jdbcTemplate.update(sqlGenreInsert, filmId, genre.getId());
        }

        return getOneById(filmId).get();
    }

    @Override
    public Film update(Film film) {
        String sql =
                    "UPDATE film " +
                    "SET " +
                        "film_name = ?, " +
                        "description = ?, " +
                        "releaseDate = ?, " +
                        "duration = ?, " +
                        "mpa_id = ? " +
                    "WHERE id = ?;";

        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        String sqlGenreRemove = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sqlGenreRemove, film.getId());

        for (Genre genre : film.getGenres()) {
            String sqlGenreInsert =
                    "INSERT INTO film_genre (film_id, genre_id) " +
                    "VALUES (?, ?);";

            jdbcTemplate.update(sqlGenreInsert, film.getId(), genre.getId());
        }

        return getOneById(film.getId()).get();
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM film WHERE id = ?;", id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlInsertLike =
                            "INSERT INTO film_user_like (film_id, user_id) " +
                            "VALUES (?, ?);";

        jdbcTemplate.update(sqlInsertLike, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlDeleteLike = "DELETE FROM film_user_like WHERE film_id = ? AND user_id = ?;";

        jdbcTemplate.update(sqlDeleteLike, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sql =
                    "SELECT " +
                        "film.id, " +
                        "film.film_name AS name, " +
                        "film.description, " +
                        "film.releaseDate, " +
                        "film.duration, " +
                        "mpa.id AS mpa_id, " +
                        "mpa.mpa_value AS mpa_name, " +
                        "COUNT(film_user_like.film_id) AS likes_quantity " +
                    "FROM film " +
                    "JOIN mpa ON film.mpa_id = mpa.id " +
                    "LEFT JOIN film_user_like ON film.id = film_user_like.film_id " +
                    "GROUP BY film.id, " +
                            "film.film_name, " +
                            "film.description, " +
                            "film.releaseDate, " +
                            "mpa.id, " +
                            "mpa.mpa_value " +
                    "ORDER BY likes_quantity DESC " +
                    "LIMIT ?;";

        String sqlGenresQuery =
                            "SELECT " +
                                "film_genre.film_id, " +
                                "film_genre.genre_id, " +
                                "genre.genre_name " +
                            "FROM film_genre " +
                            "JOIN genre ON film_genre.genre_id = genre.id;";

        List<Film> films = jdbcTemplate.query(sql, filmRowMapper, count);
        List<Film> filmsGenres = jdbcTemplate.query(sqlGenresQuery, filmGenresRowMapper);

        for (Film g : filmsGenres) {
            for (Film f : films) {
                if (f.getId().equals(g.getId())) {
                    f.getGenres().addAll(g.getGenres());
                }
            }
        }

        return films;
    }
}
