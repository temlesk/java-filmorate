package ru.yandex.practicum.filmorate.controller.handler;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FilmHandler {

    private final Map<Long, Film> films = new HashMap<>();

    private final AtomicLong generatedId = new AtomicLong(0);

    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12,28);

    public Film create(@Valid Film film) {
        check(film);
        film.setId(generatedId.incrementAndGet());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(@Valid Film film) {
        check(film);
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private void check(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getDescription().isBlank()) {
            throw new ValidationException("Описание не должно быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28))) {
            throw new ValidationException("Дата релиза - не раньше " + DATE_RELEASE);
        }
        if (film.getDuration() < 0L) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
