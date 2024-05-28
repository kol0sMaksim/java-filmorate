package ru.yandex.practicum.filmorate.helpers.annotations;

import jakarta.validation.Constraint;

import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.helpers.validators.ReleaseDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ValidReleaseDate {

    String message() default "Дата релиза не может быть раньше 28 декабря 1895 года";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
