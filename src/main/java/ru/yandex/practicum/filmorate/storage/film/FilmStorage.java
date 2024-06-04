package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAll();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getPopularFilm(int count);
}
