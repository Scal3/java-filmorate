package ru.yandex.practicum.filmorate.user.storage;

import ru.yandex.practicum.filmorate.user.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getOneById(int id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(int id);

    void addFriend(int userId, int friendId, FriendshipStatus friendshipStatus);

    boolean isUserFriendOfOtherUser(int userId, int expectedFriendId);

    void changeFriendshipStatus(int userId, FriendshipStatus friendshipStatus);

    void removeFriend(int userId, int friendId);

    List<User> getUserFriends(int userId);
}
