package ru.yandex.practicum.filmorate.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    private int id;

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
}
