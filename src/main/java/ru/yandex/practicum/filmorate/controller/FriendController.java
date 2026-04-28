package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//
//    @PutMapping("/{friendId}")
//    public void add(@PathVariable long userId, @PathVariable long friendId) {
//        friendService.addFriend(userId, friendId);
//    }

    @GetMapping
    public Set<User> get(@PathVariable long userId) {
        return friendService.getFriends(userId);
    }

    @GetMapping("/common/{otherId}")
    public Set<User> getCommon(@PathVariable long userId, @PathVariable long otherId) {
        return friendService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{friendId}")
    public void delete(@PathVariable long userId, @PathVariable long friendId) {
        friendService.deleteFriend(userId, friendId);
    }
}
