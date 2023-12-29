package ru.yandex.practicum.filmorate.mpa.model;

import lombok.Data;

@Data
public class Mpa {
    private Integer id;
    private String name;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa(Integer id) {
        this.id = id;
        this.name = null;
    }
}
