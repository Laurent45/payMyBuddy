package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.UserRepository;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user)
            throws EmailAlreadyUsedException, ConstraintErrorException {
        log.info("addUser method called, parameter -> User: " + user);

        if (isEmailAlreadyExist(user.getEmail())) {
            throw new EmailAlreadyUsedException("email already used: "
                    + user.getEmail());
        }

        if (user.getFirstName() == null
                || user.getLastName() == null
                || user.getEmail() == null
                || user.getPassword() == null) {
            throw new ConstraintErrorException("some fields with not " +
                    "null constraint are null");
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        log.info("getUsers method called, parameter -> none");

        return userRepository.findAll();
    }

    @Override
    public User getUserById(long idUser) throws UserUnknownException {
        log.info("getUserById method called, parameter -> idUser: " + idUser);

        return userRepository.findById(idUser)
                .orElseThrow(() -> new UserUnknownException("none user in " +
                        "DB with id: " + idUser));
    }

    @Override
    @Transactional
    public User updateUser(long id, User user)
            throws EmailAlreadyUsedException, UserUnknownException {
        log.info("updateUser method called, parameter -> idUser: " + id + "/" +
                " user: " + user);

        User currentUser = getUserById(id);

        if (user != null) {
            if (user.getFirstName() != null) {
                currentUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                currentUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null
                    && !user.getEmail().equals(currentUser.getEmail())) {

                if (isEmailAlreadyExist(user.getEmail())) {
                    throw new EmailAlreadyUsedException("email already used -> "
                            + user.getEmail());
                }
                currentUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                currentUser.setPassword(user.getPassword());
            }
            if (user.getBalance() != null) {
                currentUser.setBalance(user.getBalance());
            }
            return userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public void deleteUser(Long idUser) {
        log.info("deleteUser method called, parameter -> idUser: " + idUser);

        userRepository.deleteById(idUser);
    }

    @Override
    public User getUserByEmail(String email) throws UserUnknownException {
        log.info("getUserByEmail method is called, parameter -> email: "
                + email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserUnknownException("none user with " +
                        "this email: " + email));
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password)
            throws UserUnknownException {
        log.info("getUserByEmailAndPassword method called, parameters -> " +
                "email: " + email + "/ password: " + password);

        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UserUnknownException("none user with " +
                        "this email: " + email + " and password: " + password));
    }

    @Override
    public Set<User> getAllUsersConnected(String email)
            throws UserUnknownException {
        log.info("getAllUsersConnected method called, parameter -> email: "
                + email);
        User user = getUserByEmail(email);

        return user.getUsersConnected();
    }

    @Override
    public List<String> getEmailsOfUsersConnected(String email)
            throws UserUnknownException {
        log.info("getEmailsOfUsersConnected method called, parameter -> " +
                "email: " + email);
        Set<User> users = getAllUsersConnected(email);

        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public void manageAConnection(String emailUser, String emailUserConnected,
                                  boolean addRemove)
            throws UserUnknownException {
        log.info("deleteAConnection method called, parameters -> emailUser: "
                + emailUser + "/ emailToDelete: " + emailUserConnected + "/ " +
                "addRemove: " + addRemove);

        User user = getUserByEmail(emailUser);
        User userConnected = getUserByEmail(emailUserConnected);

        if (addRemove) {
            user.addConnection(userConnected);
        } else {
            user.removeConnection(userConnected);
        }

        userRepository.save(user);
    }

    private boolean isEmailAlreadyExist (String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
