package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserStorage userStorage,
                       MpaStorage mpaStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    public Film create(@Valid Film film) {
        checkDate(film);
        validateMpa(film.getMpa());
        validateGenres(film.getGenres());
        return filmStorage.create(film);
    }

    public Film update(@Valid Film film) {
        checkDate(film);
        validateMpa(film.getMpa());
        validateGenres(film.getGenres());
        return filmStorage.update(film);
    }

    private void checkDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза фильма");
        }
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(long filmId) {
        return filmStorage.get(filmId);
    }

    public void addLike(long filmId, long likerId) {
        checkUsersExist(likerId);
        Film updateFilm = get(filmId);
        updateFilm.addLike(likerId);
        filmStorage.update(updateFilm);
    }

    public void deleteLike(long filmId, long likerId) {
        checkUsersExist(likerId);
        get(filmId).deleteLike(likerId);
    }

    private void checkUsersExist(long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
    }

    private void validateMpa(Mpa mpa) {
        if (mpa != null) {
            if (mpaStorage.getById(mpa.getId()).isEmpty()) {
                throw new NotFoundException("Рейтинг с id " + mpa.getId() + " не найден");
            }
        }
    }

    private void validateGenres(Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }
        Set<Integer> requestIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Integer> existingIds = genreStorage.findExistingIds(requestIds);
        Set<Integer> missingIds = requestIds.stream()
                .filter(id -> !existingIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Жанры с id " + missingIds + " не найдены");
        }
    }
}