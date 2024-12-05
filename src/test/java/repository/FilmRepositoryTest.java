package repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.dal.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.dal.repository.film.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepositoryImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    private Film film = new Film(
            1L,
            "film_test",
            "description_test",
            LocalDate.parse("2000-01-01"),
            100L,
            new Mpa(1L, null),
            List.of(new Genre(1L, null))
    );

    private Film newFilm;

    @BeforeEach
    public void addFilm() {
        newFilm = filmRepository.addFilm(film);
    }

    @Test
    public void createFilm() {
        assertThat(newFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(newFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(newFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(newFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(newFilm).hasFieldOrPropertyWithValue("duration", film.getDuration());

        assertEquals(newFilm.getGenres().size(), 1);
        assertEquals(newFilm.getMpa().getId(), film.getMpa().getId());
    }

    @Test
    public void getFilmById() {
        Optional<Film> filmFromBD = filmRepository.getFilmById(newFilm.getId());

        assertThat(filmFromBD).isPresent();

        Film retrievedFilm = filmFromBD.get();

        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("name", newFilm.getName());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("description", newFilm.getDescription());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("releaseDate", newFilm.getReleaseDate());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("duration", newFilm.getDuration());

        assertEquals(retrievedFilm.getGenres().size(), 1);
        assertEquals(retrievedFilm.getMpa().getId(), newFilm.getMpa().getId());
    }

    @Test
    public void getAllFilms() {
        Collection<Film> films = filmRepository.getAll();

        assertEquals(films.size(), 1);
    }

    @Test
    public void updateFilm() {
        newFilm.setName("name_update");
        Film updatedFilm = filmRepository.updateFilm(newFilm);

        assertThat(updatedFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", "name_update");
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("description", newFilm.getDescription());
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("releaseDate", newFilm.getReleaseDate());
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("duration", newFilm.getDuration());

        assertEquals(updatedFilm.getGenres().size(), 1);
        assertEquals(updatedFilm.getMpa().getId(), newFilm.getMpa().getId());
    }
}
