package com.example.filmorate.controller;

import com.example.filmorate.exception.ValidationException;
import com.example.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    @GetMapping
    public List<User> getUsers() {
        return List.of(new User());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUserModel(user);

        if (user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUserModel(user);

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
            throw new ValidationException("Some field is null");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("User email is incorrect");
        }

        if (user.getLogin().trim().isEmpty()) {
            throw new ValidationException("User login is incorrect");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday is incorrect");
        }
    }
}
