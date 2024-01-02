package ru.yandex.practicum.filmorate.mpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mpa.dao.MpaDao;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa getOneMpaById(int id) {
        Optional<Mpa> mpaOptional = mpaDao.getOneById(id);

        if (mpaOptional.isEmpty()) {
            throw new NotFoundException("Mpa is not found", "GET /mpa:id");
        }

        return mpaOptional.get();
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.getAll();
    }
}
