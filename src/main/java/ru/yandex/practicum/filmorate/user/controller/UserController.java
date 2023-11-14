package ru.yandex.practicum.filmorate.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.user.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private int userId = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(++userId);
        users.put(userId, user);

        log.debug("User {} has been created", user.getName());

        return user;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        // TODO make it later
//        if (!users.containsKey(user.getId())) throw new NotFoundException("User is not found");

        users.replace(user.getId(), user);

        log.debug("User {} has been updated", user.getName());

        return user;
    }
}
