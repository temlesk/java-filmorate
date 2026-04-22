package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

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
    public Optional<Film> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(long id) {
        if (films.remove(id) == null) {
            log.error("Фильм с id {} не найден для удаления", id);
            throw new NotFoundException("Фильма с id = " + id + " не существует");
        }
        log.info("Фильм с id {} удален", id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        throw new UnsupportedOperationException("InMemoryFilmStorage не поддерживает лайки");
    }

    @Override
    public void removeLike(long filmId, long userId) {
        throw new UnsupportedOperationException("InMemoryFilmStorage не поддерживает лайки");
    }
}
