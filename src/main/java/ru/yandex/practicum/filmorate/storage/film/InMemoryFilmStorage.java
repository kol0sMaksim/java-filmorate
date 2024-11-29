package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Autowired
    private UserStorage userStorage;

    private final Map<Long, Film> films = new ConcurrentHashMap<>();

    private long currentMaxId;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        film.setName(film.getName());
        film.setDescription(film.getDescription());
        film.setReleaseDate(film.getReleaseDate());
        film.setDuration(film.getDuration());

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            return oldFilm;
        }

        log.warn("Фильм с id={} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм с id={} не найден", id);
            throw  new NotFoundException("Фильм с id = " + id + " не найден");
        }

        return films.get(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        User user = userStorage.getUserById(userId);

        if (!films.containsKey(id)) {
            log.warn("Фильм с id={} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }

       // films.get(id).addLike(userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        User user = userStorage.getUserById(userId);

        if (!films.containsKey(id)) {
            log.warn("Фильм с id={} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }

        //films.get(id).deleteLike(userId);
    }

   /* @Override
    public Collection<Film> getPopularFilm(int count) {
        return films.values().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }*/

    private long getNextId() {
        return ++currentMaxId;
    }
}
