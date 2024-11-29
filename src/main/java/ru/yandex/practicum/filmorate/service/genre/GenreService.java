package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.Collection;

public interface GenreService {
    GenreDto getGenreById(Long id);

    Collection<GenreDto> getAll();
}
