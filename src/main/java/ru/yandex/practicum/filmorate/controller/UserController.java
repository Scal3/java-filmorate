package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public User createUser(@RequestBody User user) {
        validateUserModel(user);

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
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) throw new NotFoundException("User is not found");

        validateUserModel(user);

        users.replace(user.getId(), user);

        log.debug("User {} has been updated", user.getName());

        return user;
    }

    private void validateUserModel(User user) throws ValidationException {
        if (user.getEmail() == null
                || user.getBirthday() == null
                || user.getLogin() == null
        ) {
            log.warn("Some field is null");

            throw new ValidationException("Some field is null");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("User email is incorrect");

            throw new ValidationException("User email is incorrect");
        }

        if (user.getLogin().trim().isEmpty()) {
            log.warn("User login is empty");

            throw new ValidationException("User login is incorrect");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday is incorrect");

            throw new ValidationException("User birthday is incorrect");
        }
    }
}
