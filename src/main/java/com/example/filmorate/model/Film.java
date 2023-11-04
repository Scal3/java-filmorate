package com.example.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private Duration duration;
}