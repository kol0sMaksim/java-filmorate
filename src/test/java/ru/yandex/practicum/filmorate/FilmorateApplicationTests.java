package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {FilmController.class, UserController.class})
class FilmorateApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    private Film film = new Film(
            1L,
            "film",
            "description",
            LocalDate.parse("2000-01-01"),
            100L
    );

    private User user = new User(
            1L,
            "qwerty@yandex.ru",
            "login",
            "Maks",
            LocalDate.parse("1996-01-01")
    );

    @Test
    void test_createFilm_filmAdded() throws Exception {
        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_createFilm_givenEmptyName_badRequestReturned() throws Exception {
        film.setName("");

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenTooLongDescription_badRequestReturned() throws Exception {
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят" +
                " разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов," +
                " который за время «своего отсутствия», стал кандидатом Коломбани.");

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenInvalidReleaseDate_badRequestReturned() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenNegativeDuration_badRequestReturned() throws Exception {
        film.setDuration(-1L);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_updateFilm_filmChanged() throws Exception {
        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        put("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_getAllFilm() throws Exception {
        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("film")))
                .andExpect(jsonPath("$[0].description", is("description")))
                .andExpect(jsonPath("$[0].releaseDate", is("2000-01-01")))
                .andExpect(jsonPath("$[0].duration", is(100)))
                .andReturn();
    }

    @Test
    void test_createUser_userAdded() throws Exception {
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_createUser_givenEmptyLogin_badRequestReturned() throws Exception {
        user.setLogin("");

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createUser_givenInvalidEmail_badRequestReturned() throws Exception {
        user.setEmail("email");

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createUser_givenInvalidBirthday_badRequestReturned() throws Exception {
        user.setBirthday(LocalDate.parse("2200-01-01"));

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createUser_givenEmptyName_userAdded() throws Exception {
        user.setName(null);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("login")))
                .andReturn();
    }

    @Test
    void test_updateUser_userChanged() throws Exception {
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_getAllUser() throws Exception {
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("qwerty@yandex.ru")))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[0].name", is("Maks")))
                .andExpect(jsonPath("$[0].birthday", is("1996-01-01")))
                .andReturn();
    }
}