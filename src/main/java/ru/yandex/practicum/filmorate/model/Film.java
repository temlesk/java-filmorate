package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.model.featuresFilm.Genre;
import ru.yandex.practicum.filmorate.model.featuresFilm.Rating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private List<Genre> genres;
    private Rating rating;
    private Mpa mpa;

    private Long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @Min(1)
    private Long duration;

    private Set<Long> likes = new HashSet<>();

    public void deleteLike(long likerId) {
        likes.remove(likerId);
    }

    public void addLike(long likerId) {
        likes.add(likerId);
    }
}
