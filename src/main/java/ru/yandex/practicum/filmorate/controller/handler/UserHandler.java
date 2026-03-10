package ru.yandex.practicum.filmorate.controller.handler;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserHandler {
    private final Map<Long, User> users = new HashMap<>();
    private Long generatedId = 0L;
    private final String SYMBOL_DOG = "@";

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User update(@Valid User user) {
        check(user);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Пользователь с Id " + user.getId() + " не найден");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User create(@Valid User user) {
        check(user);
        user.setId(++generatedId);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    private void check(User user) {
        if (user.getEmail().isBlank() && user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ " + SYMBOL_DOG);
        }
        if (user.getLogin().isBlank() && user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
