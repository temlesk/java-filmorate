package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    private void fixName(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            String userLogin = user.getLogin();
            user.setName(userLogin);
        }
    }

    public User update(@Valid User user) {
        check(user);
        checkId(user.getId());
        fixName(user);
        return userStorage.update(user);
    }

    public User create(@Valid User user) {
        check(user);
        return userStorage.create(user);
    }

    private void checkId(long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException(id);
        }
    }

    private void check(User user) {
        if (user.getLogin().trim().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (Objects.isNull(user.getName()) || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public User get(long userId) {
        return Optional.ofNullable(userStorage.get(userId))
                .orElseThrow(() -> new NotFoundException(userId));
    }
}
