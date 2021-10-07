package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;

import java.util.List;
import java.util.Set;

public interface IUserService {

    /**
     * Create - add a new User.
     * @param user - a user Object
     * @return A user object saved in database
     * @throws EmailAlreadyUsedException - email already used
     */
    User addUser(User user) throws EmailAlreadyUsedException, ConstraintErrorException;

    /**
     * Read - get all users.
     * @return An iterable object of User
     */
    List<User> getUsers();

    /**
     * Read - get a user by id.
     * @param idUser - id's user
     * @return An optional user object
     */
    User getUserById(long idUser) throws UserUnknownException;

    /**
     * Update - update an existing user.
     * @param id - id's user to update
     * @param user - a user object with new fields
     * @return A user object updated
     */
    User updateUser(long id, User user) throws EmailAlreadyUsedException, UserUnknownException;

    /**
     * Delete - delete a user by id.
     * @param idUser - id's user to delete
     */
    void deleteUser(Long idUser);

    /**
     * Read - get a user by email
     * @param email - a string that represents email
     * @return An optional user object
     */
    User getUserByEmail(String email) throws UserUnknownException;

    /**
     * Read - get a user by its email and password
     * @param email email's user
     * @param password password'user
     * @return a user object
     * @throws UserUnknownException none user in DB whit this parameter
     */
    User getUserByEmailAndPassword(String email, String password)
    throws UserUnknownException;

    /**
     * Read - get all users connected of the user identified by its email.
     * @param email - email's user
     * @return A user list of users connected
     * @throws UserUnknownException - email identified none user
     */
    Set<User> getAllUsersConnected(String email) throws UserUnknownException;

    /**
     * Read - get all emails of users connected of the user identified by its
     * email.
     * @param email - email's user
     * @return An email list of users connected
     */
    List<String> getEmailsOfUsersConnected(String email) throws UserUnknownException;

    /**
     * Update - add or delete a connection
     * @param emailUser - email's user
     * @param emailUserConnected - email user connected to manage
     * @param addRemove - true -> add / false -> remove
     */
    void manageAConnection(String emailUser, String emailUserConnected,
                           boolean addRemove)
            throws UserUnknownException;
}
