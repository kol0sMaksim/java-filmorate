package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAll();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void addFriends(Long id, Long friendId);

    void deleteFriends(Long id, Long friendId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);
}
