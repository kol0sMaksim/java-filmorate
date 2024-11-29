package ru.yandex.practicum.filmorate.dal.repository.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class GenreRepositoryImpl extends BaseRepository<Genre> implements GenreRepository {
    private static final String FIND_ALL_GENRE = "SELECT * FROM genre";
    private static final String FIND_BY_ID_GENRE = "SELECT * FROM genre WHERE id = ?";

    public GenreRepositoryImpl(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        log.debug("Выполняется запрос в БД на получение жанра с id={}", id);
        return findOne(FIND_BY_ID_GENRE, id);
    }

    @Override
    public Collection<Genre> getAll() {
        log.debug("Выполняется запрос В БД на получение всех жанров");
        return findMany(FIND_ALL_GENRE);
    }
}
