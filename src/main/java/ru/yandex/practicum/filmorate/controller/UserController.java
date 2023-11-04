package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsers() {
        return List.of(new User());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUserModel(user);
        log.debug("User {} has been created", user.getName());

        if (user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUserModel(user);
        log.debug("User {} has been updated", user.getName());

        if (user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    private void validateUserModel(User user) throws ValidationException {
        if (user.getName() == null
                || user.getEmail() == null
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
            log.warn("User login is incorrect");

            throw new ValidationException("User login is incorrect");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday is incorrect");

            throw new ValidationException("User birthday is incorrect");
        }
    }
}
