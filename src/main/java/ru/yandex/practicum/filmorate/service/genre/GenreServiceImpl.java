package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;
    private GenreMapper genreMapper;

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Ищем жанр с id={}", id);

        return genreRepository.getGenreById(id)
                .map(genreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с id: " + id));
    }

    @Override
    public Collection<GenreDto> getAll() {
        log.info("Ищем список всех жанров фильма");

        return  genreRepository.getAll().stream()
                .map(genreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }
}
