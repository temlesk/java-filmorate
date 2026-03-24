package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update(@Valid User user) {
        check(user);
        return userStorage.update(user);
    }

    public User create(@Valid User user) {
        check(user);
        return userStorage.create(user);
    }

    public void check(User user) {
        if (user.getLogin().trim().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (Objects.isNull(user.getName()) || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public User get(long userId) {
        return userStorage.get(userId);
    }

    private void fixName(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            String userLogin = user.getLogin();
            user.setName(userLogin);
        }
    }

    public Set<User> getFriends(long userId) {
        User user = get(userId);
        return user.getFriends().stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        Set<Long> commonIds = getCommonIds(userId, otherId);
        return commonIds.stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    private Set<Long> getCommonIds(long userId, long otherId) {
        Set<Long> userFriends = get(userId).getFriends();
        Set<Long> otherFriends = get(otherId).getFriends();
        Set<Long> common = new HashSet<>(userFriends);
        common.retainAll(otherFriends);
        return common;
    }

    public void addFriend(long userId, long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }


    public void deleteFriend(long userId, long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }
}
