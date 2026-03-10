package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.handler.FilmHandler;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmHandler handler;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начато создание фильма {}", film);
        return handler.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Начато обновление фильма {}", film);
        return handler.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        return handler.getAll();
    }
}
