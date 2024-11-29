package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.mpa.MpaServise;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    @Autowired
    private MpaServise mpaServise;

    @GetMapping
    public Collection<MpaDto> getAll() {
        return mpaServise.getAll();
    }

    @GetMapping("/{id}")
    public MpaDto getMpaById(@PathVariable Long id) {
        return mpaServise.getMpaById(id);
    }
}
