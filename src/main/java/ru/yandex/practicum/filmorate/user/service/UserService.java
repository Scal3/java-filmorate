package ru.yandex.practicum.filmorate.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.user.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage storage;

    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public User getOneById(int id) {
        return getUserOrThrowException(id, "User is not found", "GET /users/{id}");
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return storage.create(user);
    }

    public User update(User user) {
        getUserOrThrowException(user.getId(), "User is not found", "PUT /users");

        return storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId)
            throw new BadRequestException("Can not add yourself", "PUT /users/{id}/friends/{friendId}");

        User user = getUserOrThrowException(userId, "User is not found", "PUT /users/{id}/friends/{friendId}");
        User friend = getUserOrThrowException(friendId, "Friend is not found", "PUT /users/{id}/friends/{friendId}");

        if (storage.isUserFriendOfOtherUser(friend.getId(), user.getId())) {
            storage.addFriend(userId, friendId, FriendshipStatus.APPROVED);
        } else {
            storage.addFriend(userId, friendId, FriendshipStatus.UNAPPROVED);
        }
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId)
            throw new BadRequestException("Can not remove yourself", "DELETE /users/{id}/friends/{friendId}");

        User user = getUserOrThrowException(userId, "User is not found", "DELETE /users/{id}/friends/{friendId}");
        User friend = getUserOrThrowException(friendId, "Friend is not found", "DELETE /users/{id}/friends/{friendId}");

        if (storage.isUserFriendOfOtherUser(user.getId(), friend.getId())) {
            storage.removeFriend(userId, friendId);
        } else {
            throw new BadRequestException(
                    "User with id " + friend.getId() + " is not your friend",
                    "DELETE /users/{id}/friends/{friendId}"
            );
        }

        if (storage.isUserFriendOfOtherUser(friend.getId(), user.getId())) {
            storage.changeFriendshipStatus(friend.getId(), FriendshipStatus.UNAPPROVED);
        }
    }

    public List<User> getUserFriends(int userId) {
        return storage.getUserFriends(userId);
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        User user1 = getUserOrThrowException(userId, "User is not found", "GET /users/{id}/friends/common/{otherId}");
        User user2 = getUserOrThrowException(
                otherUserId, "Other user is not found", "GET /users/{id}/friends/common/{otherId}");

        Set<User> intersectionSet = new HashSet<>(storage.getUserFriends(user1.getId()));
        intersectionSet.retainAll(storage.getUserFriends(user2.getId()));

        return new ArrayList<>(intersectionSet);
    }

    private User getUserOrThrowException(int id, String errMessage, String endpoint) {
        Optional<User> optionalUser = storage.getOneById(id);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException(errMessage, endpoint);
        }

        return optionalUser.get();
    }
}
