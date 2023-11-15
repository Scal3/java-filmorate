package ru.yandex.practicum.filmorate.film.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.film.validation.ReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Data
public class Film {
    private int id;

    @NotBlank(message = "Name must contain characters")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotBlank(message = "Description must contain characters")
    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    @NotNull(message = "ReleaseDate cannot be null")
    @ReleaseDateConstraint
    @Past(message = "ReleaseDate must be in the past")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive value")
    private int duration;

    private Set<Integer> userLikesList;
}
