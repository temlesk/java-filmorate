package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Film create(Film film) {
        film.setId(++idCounter);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.error("Попытка обновить несуществующий фильм с id: {}", film.getId());
            throw new NotFoundException("Фильма с id = " + film.getId() + " несуществует");
        }
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
            throw new NotFoundException("Фильма с id =" + filmId + " не найден");
        }
        return films.get(filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(
                        f2.getLikes() != null ? f2.getLikes().size() : 0,
                        f1.getLikes() != null ? f1.getLikes().size() : 0
                ))
                .limit(count)
                .collect(Collectors.toList());
    }
}
