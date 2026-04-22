//package ru.yandex.practicum.filmorate.storage.db;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.mapper.RatingMapper;
//import ru.yandex.practicum.filmorate.model.Rating;
//import ru.yandex.practicum.filmorate.storage.RatingStorage;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class RatingDbStorage implements RatingStorage {
//    private final JdbcTemplate jdbcTemplate;
//    private final RatingMapper ratingMapper;
//
//    public RatingDbStorage(JdbcTemplate jdbcTemplate, RatingMapper ratingMapper) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.ratingMapper = ratingMapper;
//    }
//
//    @Override
//    public List<Rating> getAll() {
//        String sql = "SELECT * FROM rating ORDER BY rating_id";
//        return jdbcTemplate.query(sql, ratingMapper);
//    }
//
//    @Override
//    public Optional<Rating> getById(int id) {
//        String sql = "SELECT * FROM rating WHERE rating_id = ?";
//        List<Rating> ratings = jdbcTemplate.query(sql, ratingMapper, id);
//        return ratings.stream().findFirst();
//    }
//
//    public Rating getRatingOrThrow(int id) {
//        return getById(id)
//                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + id + " не найден"));
//    }
//}
