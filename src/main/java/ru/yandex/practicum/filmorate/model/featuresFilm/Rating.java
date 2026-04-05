package ru.yandex.practicum.filmorate.model.featuresFilm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Rating {
    private int id;
    private String name;
    private String description;
}
