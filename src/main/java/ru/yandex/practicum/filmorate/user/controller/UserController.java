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

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getOneById(id);
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

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        log.debug("User with id {} and user with id {} now are friends", id, friendId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        log.debug("User with id {} and user with id {} now are not friends", id, friendId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
