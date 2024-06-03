package ru.yandex.practicum.filmorate.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements  UserService{

    private UserStorage userStorage;

    @Override
    public Collection<User> getAllUser() {
        log.info("Выполняется запрос на получение списка пользователей");
        return userStorage.getAll();
    }

    @Override
    public User save(User user) {
        validationUserName(user);

        log.info("Выполнен запрос на создание пользователя с id={}", user.getId());
        return userStorage.addUser(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            log.warn("Выполнен запрос на обновление пользователя без id");
            throw new ValidationException("Id пользователя должен быть указан");
        }

        log.info("Выполнен запрос на обновление информации по юзеру с id={}", user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Выполнен запрос на получения пользователя с id={}", id);
        return userStorage.getUserById(id);
    }

    @Override
    public void addFriends(Long id, Long friendId) {
        if (id.equals(friendId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }

        log.info("Выполнен запрос на добавление в друзья пользователей с id={},{}", id, friendId);
        userStorage.addFriends(id, friendId);
    }

    @Override
    public void deleteFriends(Long id, Long friendId) {
        if (id.equals(friendId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя удалить самого себя из друзей");
        }

        log.info("Выполнен запрос на удаление из друзей пользователей с id={},{}", id, friendId);
        userStorage.deleteFriends(id, friendId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        log.info("Выполнен запрос на получения друзей пользователя id={}", id);

        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        if (id.equals(otherId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя просмотреть общих друзей с самим собой");
        }

        log.info("Выполнен запрос на просмотр общих друзей пользователей с id={},{}", id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public static void validationUserName(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
            log.debug(
                    "В запросе на обновление пользователя с id={} отсутствует поле name, будет использовано поле login",
                    user.getId()
            );
        }
    }
}
