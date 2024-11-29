package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {

    Collection<FilmDto> getAllFilm();

    FilmDto save(FilmDto film);

    FilmDto update(Film film);

    FilmDto getFilmById(Long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<FilmDto> getPopularFilm(int count);
}
