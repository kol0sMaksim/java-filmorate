package ru.yandex.practicum.filmorate.dal.repository.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class MpaRepositoryImpl extends BaseRepository<Mpa> implements MpaRepository {
    private static final String FIND_ALL_MPA = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_MAP = "SELECT * FROM mpa WHERE id = ?";

    public MpaRepositoryImpl(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        log.debug("Выполняется запрос в БД на получение рейтинга с id={}", id);
        return findOne(FIND_BY_ID_MAP, id);
    }

    @Override
    public Collection<Mpa> getAll() {
        log.debug("Выполняется запрос В БД на получение всех рейтингов фильма");
        return findMany(FIND_ALL_MPA);
    }
}
