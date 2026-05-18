package ru.yandex.practicum.filmorate.exception.handler;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private final String type;
    private final String description;
}