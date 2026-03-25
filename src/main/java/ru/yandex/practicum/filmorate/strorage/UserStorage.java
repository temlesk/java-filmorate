package ru.yandex.practicum.filmorate.strorage;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(@Valid User user);

    List<User> getAll();

    Optional<User> get(long userId);
}
