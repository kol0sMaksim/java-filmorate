package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
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

        List <Genre> genres = new ArrayList<>();
        Set<Long> genreIds = new HashSet<>();
        do {
            long genreId = rs.getLong("genre_id");
            if (genreId != 0 && !genreIds.contains(genreId)) {
                Genre genre = new Genre(genreId, rs.getString("genre_name"));
                genres.add(genre);
                genreIds.add(genreId);
            }
        } while (rs.next() && rs.getLong("film_id") == film.getId());

        film.setGenres(genres);

        return film;
    }
}
