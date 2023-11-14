package ru.yandex.practicum.filmorate.user.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

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
        if (getOneById(user.getId()) == null) {
             throw new NotFoundException("User is not found", "PUT /users");
        }

        return storage.update(user);
    }

    // TODO
//    public void addFriend(int userId, int friendId) {
//
//    }

    // TODO
//    public void removeFriend(int userId, int friendId) {
//
//    }

    // TODO
//    public List<User> getUsersFriends(int userId) {
//
//    }

    // TODO
//    public List<User> getMutualFriends() {
//
//    }
}
