package ru.yandex.practicum.filmorate.user.storage;

import org.springframework.stereotype.Component;
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
    public User getOneById(int id) {
        return users.get(id);
    }

    @Override
    public User getOneByName(String name) {
        Optional<User> userOptional = users.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();

        return userOptional.orElse(null);
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
}
