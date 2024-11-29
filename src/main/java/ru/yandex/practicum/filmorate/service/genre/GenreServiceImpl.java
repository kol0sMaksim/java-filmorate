package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService{

    private GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        log.debug("Выполнен запрос на получение жанра фильма с id={}", id);

        return genreRepository.getGenreById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с id: " + id));
    }

    @Override
    public Collection<GenreDto> getAll() {
        log.debug("Выполнен запрос на получение списка всех всех жанров фильма");

        return  genreRepository.getAll().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }
}
