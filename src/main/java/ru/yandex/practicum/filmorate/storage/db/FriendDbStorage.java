package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Slf4j
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
        String checkSql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer userCount = jdbcTemplate.queryForObject(checkSql, Integer.class, userId);
        Integer friendCount = jdbcTemplate.queryForObject(checkSql, Integer.class, friendId);
        if (userCount == null || userCount == 0 || friendCount == null || friendCount == 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        String sql = "INSERT INTO friendships (user_id, friend_id, confirmed) VALUES (?, ?, true)";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DuplicateKeyException e) {
            log.debug("Дружба уже существует: {} -> {}", userId, friendId);
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
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
}
