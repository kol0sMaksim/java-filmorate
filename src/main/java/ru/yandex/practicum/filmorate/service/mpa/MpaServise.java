package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.util.Collection;

public interface MpaServise {
    MpaDto getMpaById(Long id);

    Collection<MpaDto> getAll();
}
