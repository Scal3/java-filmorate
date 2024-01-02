package ru.yandex.practicum.filmorate.genre.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.genre.dao.GenreDao;
import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreService {
    private final GenreDao genreDao;

    public Genre getOneGenreById(int id) {
        Optional<Genre> optionalGenre = genreDao.getOneById(id);

        if (optionalGenre.isEmpty()) {
            throw new NotFoundException("Genre is not found", "GET /genres:id");
        }

        return optionalGenre.get();
    }

    public List<Genre> getAllGenre() {
        return genreDao.getAll();
    }
}
