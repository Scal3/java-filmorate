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
        User user = storage.getOneById(id);

        if (user == null) {
            throw new NotFoundException("User is not found", "GET /users/{id}");
        }

        return user;
    }

    public User getOneByName(String name) {
        return storage.getOneByName(name);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public void create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (getOneByName(user.getName()) != null) {
            throw new BadRequestException("User already exists", "POST /users");
        }

        if (user.getFriendsIdList() == null) {
            user.setFriendsIdList(new HashSet<>());
        }

        storage.create(user);
    }

    public void update(User user) {
        if (getOneById(user.getId()) == null) {
             throw new NotFoundException("User is not found", "PUT /users");
        }

        if (user.getFriendsIdList() == null) {
            user.setFriendsIdList(new HashSet<>());
        }

        storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "PUT /users/{id}/friends/{friendId}");
        }

        User friend = getOneById(friendId);

        if (friend == null) {
            throw new NotFoundException("Friend is not found", "PUT /users/{id}/friends/{friendId}");
        }

        Set<Integer> userFriends = user.getFriendsIdList();
        userFriends.add(friend.getId());
        user.setFriendsIdList(userFriends);
        storage.update(user);

        Set<Integer> friendFriends = friend.getFriendsIdList();
        friendFriends.add(user.getId());
        friend.setFriendsIdList(friendFriends);
        storage.update(friend);
    }

    public void removeFriend(int userId, int friendId) {
        User user = getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "DELETE /users/{id}/friends/{friendId}");
        }

        User friend = getOneById(friendId);

        if (friend == null) {
            throw new NotFoundException("Friend is not found", "DELETE /users/{id}/friends/{friendId}");
        }

        Set<Integer> userFriends = user.getFriendsIdList();
        userFriends.remove(friend.getId());
        user.setFriendsIdList(userFriends);
        storage.update(user);

        Set<Integer> friendFriends = friend.getFriendsIdList();
        friendFriends.remove(user.getId());
        friend.setFriendsIdList(friendFriends);
        storage.update(friend);
    }

    public List<User> getUserFriends(int userId) {
        User user = getOneById(userId);

        if (user == null) {
            throw new NotFoundException("User is not found", "GET /users/{id}/friends");
        }

        return user.getFriendsIdList().stream()
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        User user1 = getOneById(userId);

        if (user1 == null) {
            throw new NotFoundException("User is not found", "GET /users/{id}/friends/common/{otherId}");
        }

        User user2 = getOneById(otherUserId);

        if (user2 == null) {
            throw new NotFoundException("Other user is not found", "GET /users/{id}/friends/common/{otherId}");
        }

        Set<Integer> smallIdList = user1.getFriendsIdList().size() <= user2.getFriendsIdList().size()
                ? user1.getFriendsIdList()
                : user2.getFriendsIdList();

        Set<Integer> bigIdList = user2.getFriendsIdList().size() >= user1.getFriendsIdList().size()
                ? user2.getFriendsIdList()
                : user1.getFriendsIdList();

        return smallIdList.stream()
                .filter(bigIdList::contains)
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }
}
