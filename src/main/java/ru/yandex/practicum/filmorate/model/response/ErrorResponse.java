package ru.yandex.practicum.filmorate.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String type;
    private final String description;
}
