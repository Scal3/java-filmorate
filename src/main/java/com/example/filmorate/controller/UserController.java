package com.example.filmorate.controller;

import com.example.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

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
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return user;
    }
}
