package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaServise {

    private MpaRepository mpaRepository;
    private MpaMapper mpaMapper;

    @Override
    public MpaDto getMpaById(Long id) {
        log.info("Ищем рейтинг с id={}", id);
        return mpaRepository.getMpaById(id)
                .map(mpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден с id: " + id));
    }

    @Override
    public Collection<MpaDto> getAll() {
        log.info("Ищем списк всех рейтингов");
        return mpaRepository.getAll().stream()
                .map(mpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }
}
