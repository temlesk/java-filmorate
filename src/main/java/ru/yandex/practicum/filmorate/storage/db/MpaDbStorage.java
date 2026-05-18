package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Mpa> mpaRowMapper = (rs, rowNum) ->
        new Mpa(rs.getInt("id"), rs.getString("name"));

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT id, name FROM rating ORDER BY id";
        return jdbcTemplate.query(sql, mpaRowMapper);
    }

    @Override
    public Optional<Mpa> getById(int id) {
        String sql = "SELECT id, name FROM rating WHERE id = ?";
        List<Mpa> result = jdbcTemplate.query(sql, mpaRowMapper, id);
        return result.size() == 1 ? Optional.of(result.getFirst()) : Optional.empty();
    }
}
