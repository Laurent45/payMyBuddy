package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class UserServiceIT {

    @Autowired
    private UserServiceImpl userServiceSUT;

    private User user;

    @BeforeEach
    void setUp() {
        user = null;
    }

    @AfterEach
    void tearDown() {
        if (user != null && user.getIdUser() != null) {
            userServiceSUT.deleteUser(user.getIdUser());
        }
    }

    @Test
    void givenUser_whenAddUser_thenReturnUserSaved()
            throws Exception {
        this.user = new User("Frazier"
                , "Jo"
                , "frazierjo@gmail.com"
                , "password");

        User userSaved = userServiceSUT.addUser(this.user);

        assertThat(userSaved).isEqualTo(this.user);
        assertThat(userSaved.getIdUser()).isNotZero();
        assertThat(userSaved.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void givenUserWithEmailAlreadyUsed_whenAddUser_throwEmailAlreadyUsed() {
        this.user = new User("deejay"
                , "yoan"
                , "lebronjames@gmail.com"
                , "password");

        assertThatThrownBy(() -> userServiceSUT.addUser(this.user))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    @Test
    void givenUserWithFieldsNull_whenAddUser_throwConstrainOfCreationNonRespected() {
        this.user = new User();

        assertThatThrownBy(() -> userServiceSUT.addUser(this.user))
                .isInstanceOf(ConstraintErrorException.class);
    }

    /*
        Tests of reading one or many users in DB
     */

    @Test
    void whenGetAllUsers_thenReturnIterableUsers() {
        List<User> someUsers = List.of(
                new User("harden"
                        , "james"
                        , "jamesharden@gmail.com"
                        , "password"),
                new User("allen"
                        , "ray"
                        , "rayallen@gmail.com"
                        , "password")
        );

        List<User> result = userServiceSUT.getUsers();

        assertThat(result.size()).isEqualTo(5);
        assertThat(result).containsAll(someUsers);
    }

    @Test
    void givenId_whenGetUserById_thenReturnUser() throws UserUnknownException {
        this.user = new User("harden"
                , "james"
                , "jamesharden@gmail.com"
                , "password");

        User result = userServiceSUT.getUserById(1L);

        assertThat(result).isEqualTo(this.user);
    }

    @Test
    void givenEmail_whenGetUserByEmail_thenReturnUser() throws UserUnknownException {
        this.user = new User("james"
                , "lebron"
                , "lebronjames@gmail.com"
                , "password");

        User result = userServiceSUT.getUserByEmail("lebronjames@gmail.com");

        assertThat(result).isEqualTo(this.user);
    }

    /*
        Test of updating a user in DB.
     */

    @Test
    void givenUserAndId_whenUpdateUser_thenReturnUserUpdated()
            throws Exception {
        this.user = new User("kobe"
                , "bryant"
                , "kobebryant@gmail.com"
                , "kobe_password");
        userServiceSUT.addUser(this.user);

        User userUpdate = new User("malone"
                , "karl"
                , "karlmalone@gmail.com"
                , "new_password");

        this.user = userServiceSUT.updateUser(this.user.getIdUser(), userUpdate);

        assertThat(this.user.getFirstName()).isEqualTo("karl");
        assertThat(this.user.getLastName()).isEqualTo("malone");
        assertThat(this.user.getEmail()).isEqualTo("karlmalone@gmail.com");
        assertThat(this.user.getPassword()).isEqualTo("new_password");
    }

    @Test
    void givenEmailAlreadyUsed_whenUpdateUser_throwEmailAlreadyUsed() {
        User user = new User();
        user.setEmail("mikejames@gmail.com");

        assertThatThrownBy(() -> userServiceSUT.updateUser(1L, user))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    /*
        Test of deleting a user in DB.
     */

    @Test
    @Transactional
    void givenId_whenDeleteUser_thenUpdateDB()
            throws UserUnknownException {

        User user = userServiceSUT.getUserById(3L);
        assertThat(user).isNotNull();

        userServiceSUT.deleteUser(3L);
        assertThatThrownBy(() -> userServiceSUT.getUserById(3L))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    @Transactional
    void givenEmail_whenGetEmailsOfUsersConnected_thenReturnAnEmailList()
            throws UserUnknownException {
        String email = "jamesharden@gmail.com";
        List<String> emails = userServiceSUT.getEmailsOfUsersConnected(email);

        assertThat(emails).containsAll(List.of("lebronjames@gmail.com",
                "mikejames@gmail.com", "kevindurant@gmail.com"));
    }

    @Test
    void givenEmailUnknown_whenGetEmailsOfUsersConnected_throwUserUnknownException() {
        String emailUnknown = "emailUnknown@gmail.com";
        assertThatThrownBy(() -> userServiceSUT.getEmailsOfUsersConnected(emailUnknown))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    @Transactional
    void givenEmailUserAndEmailToDelete_whenManageAConnection_thenUpdateConnectionTable()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        String emailToDelete = "lebronjames@gmail.com";
        int nbConnectionsBefore =
                userServiceSUT.getEmailsOfUsersConnected(emailUser).size();

        userServiceSUT.manageAConnection(emailUser, emailToDelete, false);

        int nbConnectionsAfter =
                userServiceSUT.getEmailsOfUsersConnected(emailUser).size();

        assertThat(nbConnectionsBefore - nbConnectionsAfter).isEqualTo(1);
    }

    @Test
    @Transactional
    void givenEmailUserAndEmailToAdd_whenManageAConnection_thenUpdateConnectionTable()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        String emailToAdd = "rayallen@gmail.com";
        int nbConnectionsBefore =
                userServiceSUT.getEmailsOfUsersConnected(emailUser).size();

        userServiceSUT.manageAConnection(emailUser, emailToAdd, true);

        int nbConnectionsAfter =
                userServiceSUT.getEmailsOfUsersConnected(emailUser).size();

        assertThat(nbConnectionsBefore + 1).isEqualTo(nbConnectionsAfter);
    }

}
