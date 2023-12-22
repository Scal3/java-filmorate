package ru.yandex.practicum.filmorate.user.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User getOneById(int id) {
        return getUserOrThrowException(id, "User is not found", "GET /users/{id}");
    }

    public User getOneByName(String name) {
        return storage.getOneByName(name);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (getOneByName(user.getName()) != null) {
            throw new BadRequestException("User already exists", "POST /users");
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

        user.getFriendsIdList().add(friend.getId());
        friend.getFriendsIdList().add(user.getId());
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUserOrThrowException(userId, "User is not found", "DELETE /users/{id}/friends/{friendId}");
        User friend = getUserOrThrowException(friendId, "Friend is not found", "DELETE /users/{id}/friends/{friendId}");

        user.getFriendsIdList().remove(friend.getId());
        friend.getFriendsIdList().remove(user.getId());
    }

    public List<User> getUserFriends(int userId) {
        User user = getUserOrThrowException(userId, "User is not found", "GET /users/{id}/friends");

        return user.getFriendsIdList().stream()
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        User user1 = getUserOrThrowException(userId, "User is not found", "GET /users/{id}/friends/common/{otherId}");
        User user2 = getUserOrThrowException(
                otherUserId, "Other user is not found", "GET /users/{id}/friends/common/{otherId}");

        Set<Integer> intersectionSet = new HashSet<>(user1.getFriendsIdList());
        intersectionSet.retainAll(user2.getFriendsIdList());

        return intersectionSet.stream()
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }

    private User getUserOrThrowException(int userId, String errMessage, String endpoint) {
        User user = storage.getOneById(userId);

        if (user == null) {
            throw new NotFoundException(errMessage, endpoint);
        }

        return user;
    }
}
