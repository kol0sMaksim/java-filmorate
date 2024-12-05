package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
public class MpaMapper {
    public Mpa mapToMpa(MpaDto mpaDto) {
        Mpa mpa = new Mpa();
        mpa.setId(mpaDto.getId());
        mpa.setName(mpaDto.getName());

        return mpa;
    }

    public MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());

        return mpaDto;
    }
}
