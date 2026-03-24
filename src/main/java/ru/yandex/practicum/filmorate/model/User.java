package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public void deleteFriend(long friendId) {
        friends.remove(friendId);
    }

    public void addFriend(long friendId) {
        friends.add(friendId);
    }
}
