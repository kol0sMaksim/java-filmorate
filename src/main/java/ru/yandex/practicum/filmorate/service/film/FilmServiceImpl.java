package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private FilmStorage filmStorage;

    @Override
    public Collection<Film> getAllFilm() {
        log.info("Выполняется запрос на получение списка фильмов");
        return filmStorage.getAll();
    }

    @Override
    public Film save(Film film) {
        log.info("Выполнен запрос на создание фильма с id={}", film.getId());
        return filmStorage.addFilm(film);
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            log.warn("Выполнен запрос на обновление фильма без id");
            throw new ValidationException("Id должен быть указан");
        }

        log.info("Выполнен запрос на обновление информации по фильму с id={}", film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Выполнен запрос на получения фильма с id={}", id);
        return filmStorage.getFilmById(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Выполнен запрос на добавление лайка от пользователя с id={}, фильму с id={}", userId, id);

        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Выполнен запрос на удаление лайка от пользователя с id={}, фильму с id={}", userId, id);

        filmStorage.deleteLike(id, userId);
    }

    @Override
    public Collection<Film> getPopularFilm(int count) {
        log.info("Выполнен запрос на получение популярных фильмов");

        Collection<Film> allFilms = filmStorage.getAll();

        return allFilms.stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
