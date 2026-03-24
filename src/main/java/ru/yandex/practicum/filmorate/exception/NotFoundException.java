package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Long id;

    public NotFoundException(Long id) {
        super(String.format("Модель с id <%d> не найдена в контейнере", id));
        this.id = id;
    }
}