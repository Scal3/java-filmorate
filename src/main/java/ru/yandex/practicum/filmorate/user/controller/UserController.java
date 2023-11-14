package ru.yandex.practicum.filmorate.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.user.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userService.create(user);
        log.debug("User {} has been created", user.getName());

        return userService.getOneByName(user.getName());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        userService.update(user);
        log.debug("User {} has been updated", user.getName());

        return userService.getOneById(user.getId());
    }
}
