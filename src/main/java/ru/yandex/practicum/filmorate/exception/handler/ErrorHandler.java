package ru.yandex.practicum.filmorate.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.QueryParameterNotValidException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final RuntimeException e) {
        log.warn("Произошла непредвиденная ошибка", e);
        return new ErrorResponse("Ошибка сервера", "Произошла непредвиденная ошибка на сервере");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValid(final MethodArgumentNotValidException e) {
        log.warn("Произошла ошибка валидации поля", e);
        FieldError fieldError = e.getFieldError();
        if (fieldError != null) {
            return new ErrorResponse("Некорректное значение параметра",
                    String.format("Значение параметра %s=%s некорректно", fieldError.getField(), fieldError.getRejectedValue()));
        } else {
            return new ErrorResponse("Некорректное значение параметра", "");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleQueryParameterNotValid(final QueryParameterNotValidException e) {
        log.warn("Получен некорректный параметр строки запроса \"{}\"={}", e.getParameterName(), e.getParameterValue(), e);
        return new ErrorResponse("Некорректный параметр строки запроса", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.warn("Объект не найден: {}", e.getMessage());
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrity(DataIntegrityViolationException e) {
        return new ErrorResponse("Нарушение целостности данных", e.getMostSpecificCause().getMessage());
    }
}