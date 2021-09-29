package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class UserServiceIT {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void givenUser_whenAddUser_thenReturnUserSaved() {
        User user = new User("Frazier"
                , "Jo"
                , "frazierjo@gmail.com"
                , "password");

        User userSaved = userService.addUser(user);
        user.setBalance(0.00F);

        assertThat(userSaved).isEqualTo(user);
        assertThat(userSaved.getIdUser()).isNotZero();

        userService.deleteUser(userSaved.getIdUser());
    }

    @Test
    void whenGetAllUsers_thenReturnIterableUsers() {
        List<User> someUsers = List.of(
                new User("harden", "james", "jamesharden@gmail.com",
                        "password", 0.00F),
                new User("allen", "ray", "rayallen@gmail.com", "password",
                        0.00F)
        );

        List<User> result = userService.getUsers();

        assertThat(result.size()).isGreaterThanOrEqualTo(4);
        assertThat(result).containsAll(someUsers);
    }

    @Test
    void givenId_whenGetUserById_thenReturnUser() {
        User user = new User("harden", "james", "jamesharden@gmail.com",
                "password");

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isNotEmpty().contains(user);
    }

    @Test
    void givenUserAndId_whenUpdateUser_thenReturnUserUpdated() {
        User user = new User();
        user.setFirstName("kevin");
        user.setLastName("durant");
        user.setEmail("kevindurant@gmail.com");

        User userUpdated = userService.updateUser(5L, user);

        assertThat(userUpdated.getLastName()).isEqualTo("durant");
        assertThat(userUpdated.getFirstName()).isEqualTo("kevin");
        assertThat(userUpdated.getEmail()).isEqualTo("kevindurant@gmail.com");
    }

    @Test
    void givenId_whenDeleteUser_thenReturn() {
        User user = new User("Bond", "James", "jamesbond@gmail.com",
                "password");
        user = userService.addUser(user);
        int usersCount = userService.getUsers().size();

        userService.deleteUser(user.getIdUser());

        assertThat(userService.getUsers().size()).isEqualTo(usersCount - 1);
    }

}
