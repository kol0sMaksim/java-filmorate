package ru.yandex.practicum.filmorate.dal.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> getGenreById(Long id);

    Collection<Genre> getAll();

}
