package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    private String name;
    String description;
    private LocalDate releaseDate;
    private Long duration;
}
