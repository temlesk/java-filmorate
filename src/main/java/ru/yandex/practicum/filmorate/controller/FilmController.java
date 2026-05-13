package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начато создание фильма {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Начато обновление фильма {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PutMapping("/{id}/like/{likerId}")
    public void addLike(@PathVariable long filmId, @PathVariable long likerId) {
        filmService.addLike(filmId, likerId);
    }

    @GetMapping("/{filmId}")
    public Film get(@PathVariable long filmId) {
        return filmService.get(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }

    @DeleteMapping("/{filmId}/like/{likerId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long likerId) {
        filmService.deleteLike(filmId, likerId);
    }
}
