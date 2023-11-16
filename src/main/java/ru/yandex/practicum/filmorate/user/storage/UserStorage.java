package ru.yandex.practicum.filmorate.user.storage;

import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;

public interface UserStorage {
    User getOneById(int id);

    User getOneByName(String name);

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(int id);
}
