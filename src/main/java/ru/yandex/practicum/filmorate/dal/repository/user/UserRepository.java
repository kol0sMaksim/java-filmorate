package ru.yandex.practicum.filmorate.dal.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long id);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);
}
