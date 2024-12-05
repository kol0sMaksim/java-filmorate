package ru.yandex.practicum.filmorate.dal.repository.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Friendship;

@Slf4j
@Component
public class FriendsRepositoryImpl extends BaseRepository<Friendship> implements FriendsRepository {


    private static final String ADD_FRIENDS = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

    private static final String DELETE_FRIENDS = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public FriendsRepositoryImpl(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriends(Long id, Long friendId) {
        log.debug("Выполнен запрос в БД на добавление в друзья userId={}, friendId={}", id,friendId);
        jdbc.update(ADD_FRIENDS, id, friendId);
    }

    @Override
    public void deleteFriends(Long id, Long friendId) {
        log.debug("Выполнен запрос в БД на удаление из друзей userId={}, friendId={}", id,friendId);
        jdbc.update(DELETE_FRIENDS, id, friendId);
    }
}
