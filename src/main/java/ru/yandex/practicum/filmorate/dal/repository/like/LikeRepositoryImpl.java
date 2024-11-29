package ru.yandex.practicum.filmorate.dal.repository.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Like;

@Slf4j
@Component
public class LikeRepositoryImpl extends BaseRepository<Like> implements LikeRepository{

    public LikeRepositoryImpl(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    private static final String  ADD_LIKE = "insert into likes (film_id, user_id) values (?, ?)";

    private static final String DELETE_LIKE = "delete from likes where film_id = ? and user_id = ?";

    @Override
    public void addLike(Long id, Long userId) {
        log.debug("Выполнен запрос в БД на просталвение лайка фильму filmId={}, userId={}", id, userId);
        jdbc.update(ADD_LIKE, id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.debug("Выполнен запрос в БД на удаление лайка filmId={}, userId={}", id, userId);
        jdbc.update(DELETE_LIKE, id, userId);
    }
}
