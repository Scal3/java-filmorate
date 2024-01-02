package ru.yandex.practicum.filmorate.mpa.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mpa.dao.MpaDao;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Mpa> mpaRowMapper = (rs, rowNum) -> {
        return new Mpa(rs.getInt("id"), rs.getString("mpa_value"));
    };

    @Override
    public Optional<Mpa> getOneById(int id) {
        String sqlMpaQuery = "SELECT * FROM mpa WHERE id = ?;";

        return jdbcTemplate.query(sqlMpaQuery, mpaRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Mpa> getAll() {
        String sqlMpaQuery = "SELECT * FROM mpa;";

        return jdbcTemplate.query(sqlMpaQuery, mpaRowMapper);
    }
}
