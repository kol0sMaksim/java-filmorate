package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();

    private long currentMaxId;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Выполняется запрос на получение списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        film.setName(film.getName());
        film.setDescription(film.getDescription());
        film.setReleaseDate(film.getReleaseDate());
        film.setDuration(film.getDuration());

        films.put(film.getId(), film);

        log.info("Выполнен запрос на создание фильма с id={}", film.getId());

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Выполнен запрос на обновление фильма без id");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            log.info("Выполнен запрос на обновление информации по фильму с id={}", newFilm.getId());

            return oldFilm;
        }

        log.warn("Фильм с id={} не найден", newFilm.getId());
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        return ++currentMaxId;
    }
}
