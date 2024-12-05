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
import ru.yandex.practicum.filmorate.dal.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.dal.repository.user.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepositoryImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    private User user = new User(
            1L,
            "qwerty@yandex.ru",
            "login",
            "Maks",
            LocalDate.parse("1996-01-01"),
            null
    );

    private User newUser;

    @BeforeEach
    public void addFilm() {
        newUser = userRepository.addUser(user);
    }

    @Test
    public void createUser() {
        assertThat(newUser).hasFieldOrPropertyWithValue("id", newUser.getId());
        assertThat(newUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(newUser).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(newUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(newUser).hasFieldOrPropertyWithValue("email", user.getEmail());

    }

    @Test
    public void getUserById() {
        Optional<User> userById = userRepository.getUserById(newUser.getId());

        assertThat(userById).isPresent();

        User retrievedFilm = userById.get();

        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("id", newUser.getId());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("name", newUser.getName());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("login", newUser.getLogin());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("birthday", newUser.getBirthday());
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("email", newUser.getEmail());
    }

    @Test
    public void getAllUsers() {
        User anotherUser  = new User(
                2L,
                "another_email@yandex.ru",
                "login2",
                "Maksim",
                LocalDate.parse("1995-01-01"),
                null
        );
        userRepository.addUser(anotherUser);

        Collection<User> users = userRepository.getAll();

        assertEquals(users.size(), 2);
    }

    @Test
    public void updateUser() {
        newUser.setName("Максим");

        User updatedUserFromBD = userRepository.updateUser(newUser);

        assertThat(updatedUserFromBD).hasFieldOrPropertyWithValue("id", user.getId());
        assertThat(updatedUserFromBD).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(updatedUserFromBD).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(updatedUserFromBD).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
        assertThat(updatedUserFromBD).hasFieldOrPropertyWithValue("email", user.getEmail());
    }
}
