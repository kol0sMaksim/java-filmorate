package ru.yandex.practicum.filmorate.dal.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class FilmRepositoryImpl extends BaseRepository<Film> implements FilmRepository {
    private static final String FIND_ALL_FILM = "SELECT f.id AS film_id,\n" +
            "       f.name AS film_name,\n" +
            "       f.description AS film_description,\n" +
            "       f.release_date AS film_release_date,\n" +
            "       f.duration AS film_duration,\n" +
            "       m.id AS mpa_id,\n" +
            "       m.name AS mpa_name,\n" +
            "       GROUP_CONCAT(DISTINCT g.id) AS genre_ids,\n" +
            "       GROUP_CONCAT(DISTINCT g.name) AS genre_names\n" +
            "FROM films f\n" +
            "LEFT JOIN mpa m ON f.mpa_id = m.id\n" +
            "LEFT JOIN film_genre fg ON f.id = fg.film_id\n" +
            "LEFT JOIN genre g ON fg.genre_id = g.id\n" +
            "GROUP BY f.id;\n";

    private static final String CREATE_FILM = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM = "UPDATE films SET name=?, " +
            "description=?, release_date=?, duration=?, mpa_id=? WHERE id=?";

    private static final String FIND_BY_ID_FILM = "SELECT f.id AS film_id, \n" +
            "       f.name AS film_name, \n" +
            "       f.description AS film_description, \n" +
            "       f.release_date AS film_release_date, \n" +
            "       f.duration AS film_duration, \n" +
            "       m.id AS mpa_id, \n" +
            "       m.name AS mpa_name,\n" +
            "       GROUP_CONCAT(g.id) AS genre_ids, \n" +
            "       GROUP_CONCAT(g.name) AS genre_names\n" +
            "FROM films f\n" +
            "LEFT JOIN mpa m ON f.mpa_id = m.id\n" +
            "LEFT JOIN film_genre fg ON f.id = fg.film_id\n" +
            "LEFT JOIN genre g ON fg.genre_id = g.id\n" +
            "WHERE f.id = ?\n" +
            "GROUP BY f.id;";

    private static final String GET_POPULAR_FILMS = "SELECT f.id AS film_id,\n" +
            "       f.name AS film_name,\n" +
            "       f.description AS film_description,\n" +
            "       f.release_date AS film_release_date,\n" +
            "       f.duration AS film_duration,\n" +
            "       m.id AS mpa_id,\n" +
            "       m.name AS mpa_name,\n" +
            "       GROUP_CONCAT(DISTINCT g.id) AS genre_ids,\n" +
            "       GROUP_CONCAT(DISTINCT g.name) AS genre_names\n" +
            "FROM films f\n" +
            "LEFT JOIN mpa m ON f.mpa_id = m.id\n" +
            "LEFT JOIN film_genre fg ON f.id = fg.film_id\n" +
            "LEFT JOIN genre g ON fg.genre_id = g.id\n" +
            "LEFT JOIN (\n" +
            "    SELECT film_id, COUNT(user_id) AS like_count\n" +
            "    FROM likes\n" +
            "    GROUP BY film_id\n" +
            ") l ON f.id = l.film_id\n" +
            "GROUP BY f.id\n" +
            "ORDER BY l.like_count DESC\n" +
            "LIMIT ?;";

    private static final String ADD_FILM_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    public FilmRepositoryImpl(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("Выполнен запрос в БД на получение списка всех фильмов");
        return findMany(FIND_ALL_FILM);
    }

    @Override
    public Film addFilm(Film film) {
        log.debug("Выполнен запрос в БД на добавление нового фильма");
        long id = insert(
                CREATE_FILM,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        for (Genre genre : film.getGenres()) {
            update(ADD_FILM_GENRE, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Выполнен запрос в БД на обновление фильма с id={}", film.getId());
        update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        log.debug("Выполнен запрос в БД на получение фильма с id={}", id);
        return findOne(FIND_BY_ID_FILM, id);
    }

    @Override
    public Collection<Film> getPopularFilm(int count) {
        log.debug("Выполнен запрос в БД на получение списка самых популярных фильмов");
        return findMany(GET_POPULAR_FILMS, count);
    }
}
