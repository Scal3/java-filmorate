package ru.yandex.practicum.filmorate.mpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.service.MpaService;

import java.util.List;

@RestController()
@RequestMapping("/mpa")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaController {
    private final MpaService service;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public Mpa getOneMpaById(@PathVariable("id") int id) {
        return service.getOneMpaById(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    public List<Mpa> getAllMpa() {
        return service.getAllMpa();
    }
}
