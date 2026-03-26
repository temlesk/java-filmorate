package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PutMapping("/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void add(@PathVariable long userId, @PathVariable long friendId) {
        friendService.addFriend(userId, friendId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<User> get(@PathVariable long userId) {
        return friendService.getFriends(userId);
    }

    @GetMapping("/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommon(@PathVariable long userId, @PathVariable long otherId) {
        return friendService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/friendId")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void delete(@PathVariable long userId, @PathVariable long friendId) {
        friendService.deleteFriend(userId, friendId);
    }
}
