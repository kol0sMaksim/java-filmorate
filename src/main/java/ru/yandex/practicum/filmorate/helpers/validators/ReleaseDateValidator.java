package ru.yandex.practicum.filmorate.helpers.validators;

import ru.yandex.practicum.filmorate.helpers.annotations.ValidReleaseDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {

    @Override
    public void initialize(ValidReleaseDate constraint) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return value.isAfter(minDate) || value.isEqual(minDate);
    }
}
