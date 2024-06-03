package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.user.UserServiceImpl.validationUserName;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    private long currentMaxId;

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        user.setEmail(user.getEmail());
        user.setLogin(user.getLogin());
        user.setName(user.getName());
        user.setBirthday(user.getBirthday());

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            validationUserName(newUser);

            User oldUser = users.get(newUser.getId());

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());

            return oldUser;
        }

        log.warn("Пользователь с id={} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id={} не найден", id);
            throw new NotFoundException("Пользователя с id = " + id + " не найден");
        }

        return users.get(id);
    }

    @Override
    public void addFriends(Long id, Long friendId) {
        checkUsers(id, friendId);

        users.get(id).addFriend(friendId);
        users.get(friendId).addFriend(id);
    }

    @Override
    public void deleteFriends(Long id, Long friendId) {
        checkUsers(id, friendId);

        users.get(id).deleteFriend(friendId);
        users.get(friendId).deleteFriend(id);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (users.containsKey(id)) {
            Set<Long> friendsId = users.get(id).getFriends();

            return friendsId.stream()
                    .map(users::get)
                    .collect(Collectors.toList());
        } else {
            log.warn("Пользователь с id={} не найден", id);
            throw new NotFoundException("Пользователя с id = " + id + " не найден");
        }
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        checkUsers(id, otherId);

        Set<Long> friendsId = users.get(id).getFriends();
        Set<Long> otherFriendsId = users.get(otherId).getFriends();

        return friendsId.stream()
                .filter(otherFriendsId::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        return ++currentMaxId;
    }

    private void checkUsers(Long id, Long otherId) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id={} не найден", id);
            throw new NotFoundException("Пользователя с id = " + id + " не найден");
        }

        if (!users.containsKey(otherId)) {
            log.warn("Пользователь с id={} не найден", otherId);
            throw new NotFoundException("Пользователя с id = " + otherId + " не найден");
        }
    }
}
