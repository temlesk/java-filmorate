package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.featuresFilm.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
        this.genreStorage = genreStorage;
    }

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
            ps.setObject(5, film.getRating() != null ? film.getRating().getId() : null);
            return ps;
        },
                keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());
            genreStorage.addGenresToFilm(id, genreIds);
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getId());
        if (rows == 0) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film get(long filmId) {
        String sql = "SELECT * FROM films WHERE id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmMapper, filmId);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        return films.getFirst();
    }
}
