package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserService {

    Collection<User> getAllUser();

    User save(User user);

    User update(User user);

    User getUserById(Long id);

    void addFriends(Long id, Long friendId);

    void deleteFriends(Long id, Long friendId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);
}
