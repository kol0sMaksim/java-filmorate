package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserService {

    Collection<UserDto> getAllUser();

    UserDto save(User user);

    User update(User user);

    UserDto getUserById(Long id);

    void addFriends(Long id, Long friendId);

    void deleteFriends(Long id, Long friendId);

    Collection<UserDto> getFriends(Long id);

    Collection<UserDto> getCommonFriends(Long id, Long otherId);
}
