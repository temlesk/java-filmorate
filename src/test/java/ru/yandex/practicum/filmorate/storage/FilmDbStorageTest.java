package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmStorage filmStorage;

    private Film testFilm;

    @BeforeEach
    void setUp() {
        testFilm = new Film();
        testFilm.setName("Test Film");
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        testFilm.setDuration(120L);
    }

    @Test
    void testCreate() {
        Film created = filmStorage.create(testFilm);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(testFilm.getName());
        assertThat(created.getDescription()).isEqualTo(testFilm.getDescription());
        assertThat(created.getReleaseDate()).isEqualTo(testFilm.getReleaseDate());
        assertThat(created.getDuration()).isEqualTo(testFilm.getDuration());
    }

    @Test
    void testGet_WhenFilmExists() {
        Film created = filmStorage.create(testFilm);

        Film found = filmStorage.get(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getName()).isEqualTo(testFilm.getName());
    }

    @Test
    void testGet_WhenFilmDoesNotExist() {
        assertThatThrownBy(() -> filmStorage.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testUpdate() {
        Film created = filmStorage.create(testFilm);

        created.setName("Updated Film");
        created.setDuration(150L);
        Film updated = filmStorage.update(created);

        assertThat(updated.getName()).isEqualTo("Updated Film");
        assertThat(updated.getDuration()).isEqualTo(150);

        // Проверяем, что изменения сохранились в БД
        Film fromDb = filmStorage.get(created.getId());
        assertThat(fromDb.getName()).isEqualTo("Updated Film");
        assertThat(fromDb.getDuration()).isEqualTo(150);
    }
}