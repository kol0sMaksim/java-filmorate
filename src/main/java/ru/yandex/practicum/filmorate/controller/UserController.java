package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Выполняется запрос на получение списка пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validationUserName(user);

        user.setId(getNextId());
        user.setEmail(user.getEmail());
        user.setLogin(user.getLogin());
        user.setName(user.getName());
        user.setBirthday(user.getBirthday());

        users.put(user.getId(), user);

        log.info("Выполнен запрос на создание пользователя с id={}", user.getId());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("Выполнен запрос на обновление пользователя без id");
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            validationUserName(newUser);

            User oldUser = users.get(newUser.getId());

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            log.info("Выполнен запрос на обновление информации по юзеру с id={}", newUser.getId());

            return oldUser;
        }
        log.warn("Пользователь с id={} не найден", newUser.getId());
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public void validationUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug(
                    "В запросе на обновление пользователя с id={} отсутствует поле name, будет использовано поле login",
                    user.getId()
            );
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
