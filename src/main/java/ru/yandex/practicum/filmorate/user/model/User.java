package ru.yandex.practicum.filmorate.user.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;

    @Email(message = "Email must be valid email address")
    private String email;

    @NotBlank(message = "Login must contain characters")
    @Size(min = 5, max = 20, message = "Login must be between 5 and 20 characters")
    private String login;

    private String name;

    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;

    private Set<Integer> friendsIdList;

    private FriendshipStatus friendshipStatus;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendsIdList = new HashSet<>();
    }
}
