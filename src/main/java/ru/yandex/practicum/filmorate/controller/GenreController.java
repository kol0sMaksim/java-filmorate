package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getAllGenre() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }
}
