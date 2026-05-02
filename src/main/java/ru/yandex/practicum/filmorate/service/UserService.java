package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

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
            throw new NotFoundException("id = " + id + "равен null");
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
                .orElseThrow(() -> new NotFoundException("Пользователся с id = " + userId + "не сущесвтует."));
    }

    public List<User> getFriends(long userId) {
        userStorage.get(userId);
        return friendStorage.getFriends(userId);
    }

    public void addFriend(long userId, long friendId) {
        userStorage.get(userId);
        userStorage.get(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }
}
