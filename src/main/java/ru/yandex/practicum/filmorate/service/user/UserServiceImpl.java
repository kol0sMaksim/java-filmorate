package ru.yandex.practicum.filmorate.service.user;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.friends.FriendsRepository;
import ru.yandex.practicum.filmorate.dal.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private FriendsRepository friendsRepository;

    @Override
    public Collection<UserDto> getAllUser() {
        log.info("Выполняется запрос на получение списка пользователей");
        return userRepository.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(User user) {
        validationUserName(user);
        user = userRepository.addUser(user);
        log.info("Выполнен запрос на создание пользователя с id={}", user.getId());

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            log.warn("Выполнен запрос на обновление пользователя без id");
            throw new ValidationException("Id пользователя должен быть указан");
        }

        userRepository.getUserById(user.getId());

        log.info("Выполнен запрос на обновление информации по юзеру с id={}", user.getId());

        return userRepository.updateUser(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Выполнен запрос на получения пользователя с id={}", id);

        return  userRepository.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + id));
    }

    @Override
    public void addFriends(Long id, Long friendId) {
        if (id.equals(friendId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }

        getUserById(id);
        getUserById(friendId);

        log.info("Выполнен запрос на добавление в друзья пользователей с id={},{}", id, friendId);
        friendsRepository.addFriends(id, friendId);
    }

    @Override
    public void deleteFriends(Long id, Long friendId) {
        if (id.equals(friendId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя удалить самого себя из друзей");
        }

        getUserById(id);
        getUserById(friendId);

        log.info("Выполнен запрос на удаление из друзей пользователей с id={},{}", id, friendId);
        friendsRepository.deleteFriends(id, friendId);
    }

    @Override
    public Collection<UserDto> getFriends(Long id) {
        log.info("Выполнен запрос на получения друзей пользователя id={}", id);

        getUserById(id);

        return userRepository.getFriends(id).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<UserDto> getCommonFriends(Long id, Long otherId) {
        if (id.equals(otherId)) {
            log.warn("Выполнен запрос с одинаковым id={}", id);
            throw new ValidationException("Нельзя просмотреть общих друзей с самим собой");
        }

        log.info("Выполнен запрос на просмотр общих друзей пользователей с id={},{}", id, otherId);
        return userRepository.getCommonFriends(id, otherId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
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
