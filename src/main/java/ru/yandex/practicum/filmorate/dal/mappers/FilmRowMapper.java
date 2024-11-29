package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("film_description"));
        film.setReleaseDate(rs.getDate("film_release_date").toLocalDate());
        film.setDuration(rs.getLong("film_duration"));

        Mpa mpa = new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"));
        film.setMpa(mpa);

        String genreIds = rs.getString("genre_ids");
        String genreNames = rs.getString("genre_names");
        if (genreIds != null) {
            String[] ids = genreIds.split(",");
            String[] names = genreNames.split(",");
            Set<Genre> genres = new HashSet<>();
            for (int i = 0; i < ids.length; i++) {
                Genre genre = new Genre(Long.parseLong(ids[i]), names[i]);
                genres.add(genre);
            }
            film.setGenres(new ArrayList<>(genres));
        } else {
            film.setGenres(new ArrayList<>());
        }

        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }
}