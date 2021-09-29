package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Create - add a new User.
     * @param user - a user Object
     * @return A user object saved in database
     */
    User addUser(User user);

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
    Optional<User> getUserById(long idUser);

    /**
     * Update - update an existing user.
     * @param id - id's user to update
     * @param user - a user object with new fields
     * @return A user object updated
     */
    User updateUser(long id, User user);

    /**
     * Delete - delete a user by id.
     * @param idUser - id's user to delete
     */
    void deleteUser(Long idUser);
}
