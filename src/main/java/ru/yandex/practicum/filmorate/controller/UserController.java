package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.handler.UserHandler;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserHandler handlerUser;

    @GetMapping
    public List<User> getAll() {
        return handlerUser.getAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Начато обновление юзера {}", user);
        return handlerUser.update(user);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начато добавление юзера {}", user);
        return handlerUser.create(user);
    }
}
