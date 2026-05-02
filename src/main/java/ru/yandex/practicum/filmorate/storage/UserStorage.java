package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(@Valid User user);

    List<User> getAll();

    User get(long userId);

    Optional<User> findUserById(long userId);

    void deleteUser(long userId);

    void deleteFriend(long userId, long friendId);

    List<User> getCommonFriends(long userId, long otherId);
}
