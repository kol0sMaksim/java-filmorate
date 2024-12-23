package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.helpers.annotations.ValidReleaseDate;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive
    private Long duration;

    private Mpa mpa;

    private List<Genre> genres;

    /*public void addLike(Long like) {
        likes.add(like);
    }

    public void deleteLike(Long like) {
        likes.remove(like);
    }*/
}
