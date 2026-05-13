package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreMapper genreMapper;

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);
        updateGenres(id, film.getGenres());
        return get(id);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
        if (rows == 0) throw new NotFoundException("Film not found");
        updateGenres(film.getId(), film.getGenres());
        return get(film.getId());
    }

    private void updateGenres(long filmId, Set<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        if (genres != null && !genres.isEmpty()) {
            List<Integer> genreIds = genres.stream()
                    .map(Genre::getId)
                    .distinct()
                    .toList();

            for (Integer genreId : genreIds) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genreId);
            }
        }
    }

    @Override
    public Film get(long filmId) {
        String sql = "SELECT * FROM films WHERE id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmMapper, filmId);
        if (films.isEmpty()) throw new NotFoundException("Фильм не найден");
        Film film = films.getFirst();
        loadMpa(film);
        loadGenres(film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films", filmMapper);
        for (Film film : films) {
            loadMpa(film);
            loadGenres(film);
        }
        return films;
    }

    private void loadMpa(Film film) {
        String sql = "SELECT id, name FROM rating WHERE id = (SELECT rating_id FROM films WHERE id = ?)";
        List<Mpa> result = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Mpa(rs.getInt("id"), rs.getString("name")), film.getId());
        if (!result.isEmpty()) {
            film.setMpa(result.getFirst());
        }
    }

    private void loadGenres(Film film) {
        String sql = "SELECT g.id, g.name FROM genres g " +
                "JOIN film_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? ORDER BY g.id";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, film.getId());
        film.setGenres(new LinkedHashSet<>(genres));
    }

    @Override
    public List<Film> getPopular(int count) {
        String sql = "SELECT f.*, COUNT(l.user_id) as likes_count " +
                "FROM films f " +
                "LEFT JOIN film_likes l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY likes_count DESC, f.id ASC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, filmMapper, count);
        for (Film film : films) {
            loadMpa(film);
            loadGenres(film);
            loadLikes(film);
        }
        return films;
    }

    private void loadLikes(Film film) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.queryForList(sql, Long.class, film.getId());
        film.setLikes(new HashSet<>(likes));
    }

    @Override
    public void addLike(long filmId, long userId) {
        get(filmId);
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer userCount = jdbcTemplate.queryForObject(checkUserSql, Integer.class, userId);
        if (userCount == null || userCount == 0) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }

        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        } catch (DuplicateKeyException e) {
            log.debug("Лайк уже существует: фильм {}, пользователь {}", filmId, userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        int rows = jdbcTemplate.update(sql, filmId, userId);
        if (rows == 0) {
            log.warn("Лайк не найден: фильм {}, пользователь {}", filmId, userId);
        } else {
            log.info("Пользователь {} удалил лайк с фильма {}", userId, filmId);
        }
    }
}