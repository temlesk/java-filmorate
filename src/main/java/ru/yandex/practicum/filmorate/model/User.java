package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String login;
    @NotNull
    private String name;
    @NotNull
    private LocalDate birthday;
}
