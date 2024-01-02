package ru.yandex.practicum.filmorate.user.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.user.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int userId = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public Optional<User> getOneById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(++userId);
        users.put(userId, user);

        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);

        return user;
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public void addFriend(int userId, int friendId, FriendshipStatus friendshipStatus) {

    }

    @Override
    public boolean isUserFriendOfOtherUser(int userId, int expectedFriendId) {
        return false;
    }

    @Override
    public void changeFriendshipStatus(int userId, int friendId, FriendshipStatus friendshipStatus) {

    }

    @Override
    public void removeFriend(int userId, int friendId) {

    }

    @Override
    public List<User> getUserFriends(int userId) {
        return null;
    }

    @Override
    public List<User> getMutualFriends(int userId, int otherUserId) {
        return null;
    }
}
