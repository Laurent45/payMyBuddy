package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class UserServiceIT {

    @Autowired
    private UserServiceImpl userService;

    /*
        Test of creating a user in DB
     */
    @Test
    void givenUser_whenAddUser_thenReturnUserSaved()
            throws Exception {
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
    void givenUserWithEmailAlreadyUsed_whenAddUser_throwEmailAlreadyUsed() {
        User user = new User("deejay", "yoan", "lebronjames@gmail.com",
                "password");

        assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    @Test
    void givenUserWithFieldsNull_whenAddUser_throwConstrainOfCreationNonRespected() {
        User user = new User();

        assertThatThrownBy(() -> userService.addUser(user))
                .isInstanceOf(ConstraintErrorException.class);
    }

    /*
        Tests of reading one or many users in DB
     */

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
    void givenId_whenGetUserById_thenReturnUser() throws UserUnknownException {
        User user = new User("harden", "james", "jamesharden@gmail.com",
                "password");

        User result = userService.getUserById(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void givenEmail_whenGetUserByEmail_thenReturnUser() throws UserUnknownException {
        User user = new User("james", "lebron", "lebronjames@gmail.com",
                "password", 0.00F);

        User result = userService.getUserByEmail("lebronjames@gmail.com");

        assertThat(result).isEqualTo(user);
    }

    /*
        Test of updating a user in DB.
     */

    @Test
    @Transactional
    void givenUserAndId_whenUpdateUser_thenReturnUserUpdated()
            throws EmailAlreadyUsedException, UserUnknownException {
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
    void givenEmailAlreadyUsed_whenUpdateUser_throwEmailAlreadyUsed() {
        User user = new User();
        user.setEmail("mikejames@gmail.com");

        assertThatThrownBy(() -> userService.updateUser(1L, user))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    /*
        Test of deleting a user in DB.
     */

    @Test
    @Transactional
    void givenId_whenDeleteUser_thenUpdateDB()
            throws UserUnknownException {

        User user = userService.getUserById(3L);
        assertThat(user).isNotNull();

        userService.deleteUser(3L);
        assertThatThrownBy(() -> userService.getUserById(3L))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    @Transactional
    void givenEmail_whenGetEmailsOfUsersConnected_thenReturnAnEmailList()
            throws UserUnknownException {
        String email = "jamesharden@gmail.com";
        List<String> emails = userService.getEmailsOfUsersConnected(email);

        assertThat(emails).containsAll(List.of("lebronjames@gmail.com",
                "mikejames@gmail.com", "kevindurant@gmail.com"));
    }

    @Test
    void givenEmailUnknown_whenGetEmailsOfUsersConnected_throwUserUnknownException() {
        String emailUnknown = "emailUnknown@gmail.com";
        assertThatThrownBy(() -> userService.getEmailsOfUsersConnected(emailUnknown))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    @Transactional
    void givenEmailUserAndEmailToDelete_whenManageAConnection_thenUpdateConnectionTable()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        String emailToDelete = "lebronjames@gmail.com";
        int nbConnectionsBefore =
                userService.getEmailsOfUsersConnected(emailUser).size();

        userService.manageAConnection(emailUser, emailToDelete, false);

        int nbConnectionsAfter =
                userService.getEmailsOfUsersConnected(emailUser).size();

        assertThat(nbConnectionsBefore - nbConnectionsAfter).isEqualTo(1);
    }

    @Test
    @Transactional
    void givenEmailUserAndEmailToAdd_whenManageAConnection_thenUpdateConnectionTable()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        String emailToAdd = "rayallen@gmail.com";
        int nbConnectionsBefore =
                userService.getEmailsOfUsersConnected(emailUser).size();

        userService.manageAConnection(emailUser, emailToAdd, true);

        int nbConnectionsAfter =
                userService.getEmailsOfUsersConnected(emailUser).size();

        assertThat(nbConnectionsBefore + 1).isEqualTo(nbConnectionsAfter);
    }

}
