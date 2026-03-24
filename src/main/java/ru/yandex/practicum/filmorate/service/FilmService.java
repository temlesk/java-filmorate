package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.FilmStorage;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(@Valid Film film) {
        return filmStorage.create(film);
    }

    public Film update(@Valid Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(long filmId) {
        return filmStorage.get(filmId);
    }

    public void addLike(long filmId, long likerId) {
        userStorage.checkId(likerId);
        get(filmId).addLike(likerId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteLike(long filmId, long likerId) {
        userStorage.checkId(likerId);
        get(filmId).deleteLike(likerId);
    }
}
