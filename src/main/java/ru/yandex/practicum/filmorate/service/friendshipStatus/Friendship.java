package ru.yandex.practicum.filmorate.service.friendshipStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship {
    private int userId;
    private int friendId;
    private FriendshipStatus friendshipStatus;
}
