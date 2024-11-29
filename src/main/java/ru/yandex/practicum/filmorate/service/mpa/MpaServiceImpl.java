package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaServise{

    private MpaRepository mpaRepository;

    @Override
    public MpaDto getMpaById(Long id) {
        log.debug("Выполнен запрос на получение рейтинга с id={}", id);
        return mpaRepository.getMpaById(id)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден с id: " + id));
    }

    @Override
    public Collection<MpaDto> getAll() {
        log.debug("Выполнен запрос на получение списка всех рейтингов");
        return mpaRepository.getAll().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }
}
