package ru.yandex.practicum.filmorate.dal.repository.friends;

public interface FriendsRepository {

    void addFriends(Long id, Long friendId);

    void deleteFriends(Long id, Long friendId);
}
