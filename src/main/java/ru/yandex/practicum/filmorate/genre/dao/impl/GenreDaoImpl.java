package ru.yandex.practicum.filmorate.genre.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.genre.dao.GenreDao;
import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("genre_name"));

        return genre;
    };

    @Override
    public Optional<Genre> getOneById(int id) {
        String sqlGenreQuery = "SELECT * FROM genre WHERE id = ?;";

        return jdbcTemplate.query(sqlGenreQuery, genreRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Genre> getAll() {
        String sqlGenreQuery = "SELECT * FROM genre;";

        return jdbcTemplate.query(sqlGenreQuery, genreRowMapper);
    }
}
