package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.dal.repository.like.LikeRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaServise;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;
    private MpaServise mpaServise;
    private GenreService genreService;
    private LikeRepository likeRepository;

    @Override
    public Collection<FilmDto> getAllFilm() {
        log.info("Выполняется запрос на получение списка фильмов");
        return filmRepository.getAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto save(FilmDto request) {
        log.info("Выполнен запрос на создание фильма с id={}", request.getId());

        try {
            mpaServise.getMpaById(request.getMpa().getId());
        } catch (NotFoundException e) {
            log.warn("Выполнен запрос на создание фильма с не существующим mpa_id");
            throw new ValidationException("Указанный mpa_id не существует");
        }

        for (Genre genre : request.getGenres()) {
            try {
                genreService.getGenreById(genre.getId());
            } catch (NotFoundException e) {
                log.warn("Выполнен запрос на создание фильма с не существующим genre_id");
                throw new ValidationException("Указанный genre_id не существует");
            }
        }

        Film film = FilmMapper.mapToFilm(request);

        film = filmRepository.addFilm(film);

        if (film.getMpa().getId() == null) {
            log.warn("Выполнен запрос на создание фильма с не указанным id рейтинга");
            throw new ValidationException("id рейтинга должен быть указан");
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto update(Film film) {
        log.info("Выполнен запрос на обновление фильма с id={}", film.getId());

        if (film.getId() == null) {
            log.warn("Выполнен запрос на обновление фильма без id");
            throw new ValidationException("Id должен быть указан");
        }

        Film updateFilm = filmRepository.updateFilm(film);

        return FilmMapper.mapToFilmDto(updateFilm);
    }

    @Override
    public FilmDto getFilmById(Long id) {
        log.info("Выполнен запрос на получения фильма с id={}", id);

        return filmRepository.getFilmById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с id: " + id));
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Выполнен запрос на добавление лайка от пользователя с id={}, фильму с id={}", userId, id);

        likeRepository.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Выполнен запрос на удаление лайка от пользователя с id={}, фильму с id={}", userId, id);

        likeRepository.deleteLike(id, userId);
    }

    @Override
    public Collection<Film> getPopularFilm(int count) {
        log.info("Выполнен запрос на получение популярных фильмов");

        return filmRepository.getPopularFilm(count);
    }
}
