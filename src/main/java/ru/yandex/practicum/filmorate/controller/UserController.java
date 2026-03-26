package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Начато обновление юзера {}", user);
        return userService.update(user);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начато добавление юзера {}", user);
        return userService.create(user);
    }


    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable long userId) {
        return userService.get(userId);
    }

}
