package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller;

    @BeforeEach
    public void makeController() {
        controller = new UserController();
    }

    @Test
    public void shouldCreateUser() {
        User user = new User(
                1,
                "email@email.com",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 20));

        assertEquals(user, controller.createUser(user));
    }

    @Test
    public void shouldThrowExceptionWhenFieldsAreNull() {
        User user = new User(
                1,
                null,
                null,
                null,
                null);

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        User user = new User(
                1,
                "",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 20));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsIncorrect() {
        User user = new User(
                1,
                "email",
                "login",
                "name",
                LocalDate.of(2000, Month.JULY, 20));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        User user = new User(
                1,
                "email@email.com",
                "",
                "name",
                LocalDate.of(2000, Month.JULY, 20));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsAfterThenCurrentDate() {
        User user = new User(
                1,
                "email@email.com",
                "login",
                "name",
                LocalDate.of(3000, Month.JULY, 20));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }
}