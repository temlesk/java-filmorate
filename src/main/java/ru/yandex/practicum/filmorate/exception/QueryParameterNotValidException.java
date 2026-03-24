package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class QueryParameterNotValidException extends RuntimeException {
    private final String parameterName;
    private final String parameterValue;
    private final String description;

    public QueryParameterNotValidException(String parameterName, String parameterValue, String description) {
        super(String.format("Параметр \"%s\"=%s некорректен. Причина: %s", parameterName, parameterValue, description));
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.description = description;
    }
}