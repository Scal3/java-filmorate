package ru.yandex.practicum.filmorate.user.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

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
        return storage.getOneById(id);
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

        storage.create(user);
    }

    public void update(User user) {
        if (getOneById(user.getId()) == null) {
             throw new NotFoundException("User is not found", "PUT /users");
        }

        storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = getOneById(userId);

        if (user == null) {
            // TODO add path
            throw new NotFoundException("User is not found", "path");
        }

        User friend = getOneById(friendId);

        if (friend == null) {
            // TODO add path
            throw new NotFoundException("Friend is not found", "path");
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
            // TODO add path
            throw new NotFoundException("User is not found", "path");
        }

        User friend = getOneById(friendId);

        if (friend == null) {
            // TODO add path
            throw new NotFoundException("Friend is not found", "path");
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

    public List<User> getUsersFriends(int userId) {
        User user = getOneById(userId);

        if (user == null) {
            // TODO add path
            throw new NotFoundException("User is not found", "path");
        }

        return user.getFriendsIdList().stream()
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        User user1 = getOneById(userId);

        if (user1 == null) {
            // TODO add path
            throw new NotFoundException("User is not found", "path");
        }

        User user2 = getOneById(otherUserId);

        if (user2 == null) {
            // TODO add path
            throw new NotFoundException("Other user is not found", "path");
        }

        Set<Integer> smallerIdList = user1.getFriendsIdList().size() <= user2.getFriendsIdList().size()
                ? user1.getFriendsIdList()
                : user2.getFriendsIdList();

        Set<Integer> biggerIdList = user1.getFriendsIdList().size() >= user2.getFriendsIdList().size()
                ? user1.getFriendsIdList()
                : user2.getFriendsIdList();

        return smallerIdList.stream()
                .filter(biggerIdList::contains)
                .map(storage::getOneById)
                .collect(Collectors.toList());
    }
}
