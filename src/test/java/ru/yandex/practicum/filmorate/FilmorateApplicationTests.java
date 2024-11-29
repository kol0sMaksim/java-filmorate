/*
package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {FilmController.class, UserController.class})
class FilmorateApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @MockBean
    private UserService userService;

    private Film film = new Film(
            1L,
            "film",
            "description",
            LocalDate.parse("2000-01-01"),
            100L,
            null
    );

    private User user = new User(
            1L,
            "qwerty@yandex.ru",
            "login",
            "Maks",
            LocalDate.parse("1996-01-01"),
            null
    );

    public String parsUserToString(User user) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(user);
        return json;
    }

    public String parsFilmToString(Film film) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(film);
        return json;
    }

    @Test
    void test_createFilm_filmAdded() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_createFilm_givenEmptyName_badRequestReturned() throws Exception {
        film.setName("");

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenTooLongDescription_badRequestReturned() throws Exception {
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят" +
                " разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов," +
                " который за время «своего отсутствия», стал кандидатом Коломбани.");

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenInvalidReleaseDate_badRequestReturned() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createFilm_givenNegativeDuration_badRequestReturned() throws Exception {
        film.setDuration(-1L);

        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_updateFilm_filmChanged() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        put("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_getAllFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsFilmToString(film)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_createUser_userAdded() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_createUser_givenEmptyLogin_badRequestReturned() throws Exception {
        user.setLogin("");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createUser_givenInvalidEmail_badRequestReturned() throws Exception {
        user.setEmail("email");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_createUser_givenInvalidBirthday_badRequestReturned() throws Exception {
        user.setBirthday(LocalDate.parse("2200-01-01"));

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void test_updateUser_userChanged() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test_getAllUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(parsUserToString(user)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}*/
