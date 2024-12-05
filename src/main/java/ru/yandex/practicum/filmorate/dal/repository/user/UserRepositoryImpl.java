package ru.yandex.practicum.filmorate.dal.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class UserRepositoryImpl extends BaseRepository<User> implements UserRepository {

    private static final String CREATE_USER = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

    private static final String FIND_ALL_USER = "SELECT * FROM users";

    private static final String UPDATE_USER = "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?";

    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id =?";

    private static final String GET_COMMON_FRIENDS = "SELECT *\n" +
            "FROM users\n" +
            "WHERE users.id IN (\n" +
            "    SELECT friend_id\n" +
            "    FROM friends\n" +
            "    WHERE user_id IN (?, ?) \n" +
            "      AND friend_id NOT IN (?, ?)\n" +
            ");";

    private static final String GET_FRIENDS_DY_USER = "SELECT *\n" +
            "FROM users\n" +
            "WHERE users.id IN (\n" +
            "    SELECT friend_id\n" +
            "    FROM friends\n" +
            "    WHERE user_id = ?\n" +
            ");";

    public UserRepositoryImpl(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAll() {
        log.debug("Выполнен запрос в БД на получение информации обо всех пользователях");
        return findMany(FIND_ALL_USER);
    }

    @Override
    public User addUser(User user) {
        log.debug("Выполнен запрос в БД на добовление нового пользователя");
        long id = insert(
                CREATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Выполнен запрос в БД на обновление данных пользователя id={}", user.getId());

        if (getUserById(user.getId()).isEmpty()) {
            log.warn("Пользователь с id={} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден с id: " + user.getId());
        }

        update(UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.debug("Выполнен запрос в БД на получение информации о пользователе с id={}", id);
        return findOne(GET_USER_BY_ID, id);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        log.debug("Выполнен запрос в БД на получение информации обо всех друзьях пользователях с id={}", id);
        return findMany(GET_FRIENDS_DY_USER, id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.debug(
                "Выполнен запрос в БД на получение информации об общих друзьях пользователей c id={}, {}", id, otherId
        );
        return findMany(GET_COMMON_FRIENDS, id, otherId, id, otherId);
    }
}
