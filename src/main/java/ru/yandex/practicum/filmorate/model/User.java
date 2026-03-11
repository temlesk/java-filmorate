package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotNull
    private String email;
    private String login;
    @NotNull
    private String name;
    private LocalDate birthday;
}
