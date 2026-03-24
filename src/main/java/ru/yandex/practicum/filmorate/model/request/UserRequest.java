package ru.yandex.practicum.filmorate.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class UserRequest {

    Long id;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String login;

    String name;

    @NotNull
    LocalDate birthday;
}
