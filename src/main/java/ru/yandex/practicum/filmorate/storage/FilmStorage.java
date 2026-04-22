package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film get(long filmId);

    Optional<Film> getById(long id);

    void delete(long id);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
