package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public FriendDbStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String checkSql = "SELECT COUNT(*) FROM friendships WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, friendId);
        if (count != null && count > 0) {
            return;
        }
        String sql = "INSERT INTO friendships (user_id, friend_id, confirmed) VALUES (?, ?, true)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void confirmFriend(long userId, long friendId) {
        String sql = "UPDATE friendships SET confirmed = true WHERE user_id = ? AND friend_id = ?";
        int rows = jdbcTemplate.update(sql, friendId, userId);
        if (rows == 0) {
            throw new NotFoundException("Заявка в друзья от пользователя " + friendId + " к " + userId + " не найдена");
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friendships WHERE (user_id = ? AND friend_id = ?)";
        int rows = jdbcTemplate.update(sql, userId, friendId);
        if (rows == 0) {
            throw new NotFoundException("Такой дружбы несуществует");
        }
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql = """
            SELECT u.* FROM users u
            JOIN friendships f ON u.id = f.friend_id
            WHERE f.user_id = ? AND f.confirmed = true
            ORDER BY u.id
        """;
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public List<User> getPendingRequests(long userId) {
        String sql = """
            SELECT u.* FROM users u
            JOIN friendships f ON u.id = f.user_id
            WHERE f.friend_id = ? AND f.confirmed = false
            ORDER BY u.id
        """;
        return jdbcTemplate.query(sql, userMapper, userId);
    }
}
