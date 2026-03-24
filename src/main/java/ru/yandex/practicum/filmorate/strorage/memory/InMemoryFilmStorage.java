package ru.yandex.practicum.filmorate.strorage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    private static final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(++idCounter);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.error("Попытка обновить несуществующий фильм с id: {}", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(long filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм с id {} не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        return films.get(filmId);
    }

    // Вспомогательный метод для бизнес-валидации
    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH_DATE)) {
            log.warn("Валидация не пройдена: дата релиза {} раньше 28.12.1895", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}