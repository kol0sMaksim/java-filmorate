package ru.yandex.practicum.filmorate.dal.repository.like;

public interface LikeRepository {

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);
}
