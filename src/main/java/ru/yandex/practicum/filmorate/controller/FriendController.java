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
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PutMapping("/{userId}/{friends}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        friendService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/{friends}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getFriends(@PathVariable long userId) {
        return friendService.getFriends(userId);
    }

    @GetMapping("/{userId}/{friends}/{common}/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return friendService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}/{friends}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        friendService.deleteFriend(userId, friendId);
    }
}
