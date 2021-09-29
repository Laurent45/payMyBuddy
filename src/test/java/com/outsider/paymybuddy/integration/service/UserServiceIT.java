package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
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
    }

    @Test
    void whenGetAllUsers_thenReturnIterableUsers() {
        assertThat(userService.getUserById(1).get().getLastName()).isEqualTo(
                "Harden");
        List<User> result = userService.getUsers();

        assertThat(result.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void givenId_whenGetUserById_thenReturnUser() {
        assertThat(userService.getUserById(1).get().getLastName()).isEqualTo(
                "Harden");
        assertThat(userService.getUsers().size()).isEqualTo(3);
        User user = new User("Harden", "James", "jamesharden@gmail.com",
                "password");

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isNotEmpty().contains(user);
    }

}
