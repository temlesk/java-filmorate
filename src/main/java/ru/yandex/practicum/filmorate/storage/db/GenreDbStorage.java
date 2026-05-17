package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public Optional<Genre> getById(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, id);
        return genres.stream().findFirst();
    }

    @Override
    public Set<Integer> findExistingIds(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }
        String sql = "SELECT id FROM genres WHERE id IN (%s)";
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        sql = String.format(sql, inSql);

        return jdbcTemplate.query(sql, ids.toArray(), (rs, rowNum) -> rs.getInt("id"))
                .stream()
                .collect(Collectors.toSet());
    }
}
