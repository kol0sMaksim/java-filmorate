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
    private FilmMapper filmMapper;

    @Override
    public Collection<FilmDto> getAllFilm() {
        log.info("Ищем списк фильмов");
        return filmRepository.getAll()
                .stream()
                .map(filmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto save(FilmDto request) {
        log.info("Ищем фильм с id={}", request.getId());

        try {
            mpaServise.getMpaById(request.getMpa().getId());
        } catch (NotFoundException e) {
            log.warn("Запрос на создание фильма с не существующим mpa_id");
            throw new ValidationException("Указанный mpa_id не существует");
        }

        for (Genre genre : request.getGenres()) {
            try {
                genreService.getGenreById(genre.getId());
            } catch (NotFoundException e) {
                log.warn("Запрос на создание фильма с не существующим genre_id");
                throw new ValidationException("Указанный genre_id не существует");
            }
        }

        Film film = filmMapper.mapToFilm(request);

        film = filmRepository.addFilm(film);

        if (film.getMpa().getId() == null) {
            log.warn("Запрос на создание фильма с не указанным id рейтинга");
            throw new ValidationException("id рейтинга должен быть указан");
        }

        return filmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto update(Film film) {
        log.info("Обновляем фильм с id={}", film.getId());

        if (film.getId() == null) {
            log.warn("Обновление фильма без id");
            throw new ValidationException("Id должен быть указан");
        }

        Film updateFilm = filmRepository.updateFilm(film);


        return filmMapper.mapToFilmDto(updateFilm);
    }

    @Override
    public FilmDto getFilmById(Long id) {
        log.info("Ищем фильм с id={}", id);

        return filmRepository.getFilmById(id)
                .map(filmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с id: " + id));
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Добавлем лайк от пользователя с id={}, фильму с id={}", userId, id);

        likeRepository.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Удаляем лайк от пользователя с id={}, фильму с id={}", userId, id);

        likeRepository.deleteLike(id, userId);
    }

    @Override
    public Collection<FilmDto> getPopularFilm(int count) {
        log.info("Ищем список популярных фильмов");

        return filmRepository.getPopularFilm(count)
                .stream()
                .map(filmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }
}
