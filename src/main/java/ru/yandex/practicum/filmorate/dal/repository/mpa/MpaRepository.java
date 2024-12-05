package ru.yandex.practicum.filmorate.dal.repository.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;


public interface MpaRepository {

    Optional<Mpa> getMpaById(Long id);

    Collection<Mpa> getAll();
}
