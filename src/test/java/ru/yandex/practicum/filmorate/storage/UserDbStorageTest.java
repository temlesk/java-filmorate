package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserStorage userStorage;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setLogin("testuser");
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testCreate() {
        User created = userStorage.create(testUser);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(created.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(created.getName()).isEqualTo(testUser.getName());
        assertThat(created.getBirthday()).isEqualTo(testUser.getBirthday());
    }

    @Test
    void testFindUserById_WhenUserExists() {
        User created = userStorage.create(testUser);

        Optional<User> userOptional = userStorage.findUserById(created.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(created.getId());
                    assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
                });
    }

    @Test
    void testFindUserById_WhenUserDoesNotExist() {
        Optional<User> userOptional = userStorage.findUserById(999L);

        assertThat(userOptional).isEmpty();
    }

    @Test
    void testGet_WhenUserExists() {
        User created = userStorage.create(testUser);

        User found = userStorage.get(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void testGet_WhenUserDoesNotExist() {
        assertThatThrownBy(() -> userStorage.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testUpdate() {
        User created = userStorage.create(testUser);

        created.setName("Updated Name");
        created.setEmail("updated@example.com");
        User updated = userStorage.update(created);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");

        // Проверяем, что изменения сохранились в БД
        User fromDb = userStorage.get(created.getId());
        assertThat(fromDb.getName()).isEqualTo("Updated Name");
        assertThat(fromDb.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void testDeleteUser() {
        User created = userStorage.create(testUser);

        userStorage.deleteUser(created.getId());

        assertThatThrownBy(() -> userStorage.get(created.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}