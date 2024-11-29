package ru.yandex.practicum.filmorate.dal.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {
    Collection<Film> getAll();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(Long id);

    Collection<Film> getPopularFilm(int count);
}
