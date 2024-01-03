package ru.yandex.practicum.filmorate.film.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.film.validation.ReleaseDateConstraint;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;

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
    private Integer duration;

    private Set<Integer> userLikesList;

    private Set<Genre> genres;

    private Mpa mpa;

    public Film() {
        this.userLikesList = new HashSet<>();
        this.genres = new HashSet<>();
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.userLikesList = new HashSet<>();
        this.genres = new HashSet<>();
    }
}
