package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private final UserStorage userStorage;

    @Autowired
    public FriendService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User get(long userId) {
        return Optional.ofNullable(userStorage.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователся с id = " + userId + " не существует"));
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

    public Set<User> getFriends(long userId) {
        User user = get(userId);
        return user.getFriends().stream()
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

    public Set<User> getCommonFriends(long userId, long otherId) {
        Set<Long> commonIds = getCommonIds(userId, otherId);
        return commonIds.stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }
}
