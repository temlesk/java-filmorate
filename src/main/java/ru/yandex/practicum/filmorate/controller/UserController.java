package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
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
        // конвертация данных
        return userService.create(user);
    }

    @PutMapping("/{userId}" + "/{friends}" + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable long userId) {
        return userService.get(userId);
    }

    @GetMapping("/{userId}" + "/{friends}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getFriends(@PathVariable long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}" + "{/friends}" + "/{/common}" + "/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}" + "/{friends}" + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
    }

}
