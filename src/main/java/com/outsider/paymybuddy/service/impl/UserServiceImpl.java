package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.UserRepository;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) throws EmailAlreadyUsedException, ConstraintErrorException {
        log.debug("addUser method called, parameter -> User: " + user);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("email already used");
        }

        if (user.getFirstName() == null
                || user.getLastName() == null
                || user.getEmail() == null
                || user.getPassword() == null) {
            log.error("some fields with not null constraint are null");
            throw new ConstraintErrorException("some fields with not " +
                    "null constraint are null");
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        log.debug("getUsers method called, parameter -> none");

        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(long idUser) {
        log.debug("getUserById method called, parameter -> idUser: " + idUser);

        return userRepository.findById(idUser);
    }

    @Override
    public User updateUser(long id, User user) throws EmailAlreadyUsedException {
        log.debug("updateUser method called, parameter -> idUser: " + id + "/" +
                " user: " + user);

        Optional<User> userToUpdate = userRepository.findById(id);
        if (userToUpdate.isEmpty()) {
            log.debug("none user in DB with id: " + id);
            return null;
        }

        User currentUser = userToUpdate.get();
        if (user != null) {
            if (user.getFirstName() != null) {
                currentUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                currentUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null) {
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    log.error("email already used -> " + user.getEmail());
                    throw new EmailAlreadyUsedException("email already used -> " + user.getEmail());
                }
                currentUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                currentUser.setPassword(user.getPassword());
            }
            return userRepository.save(currentUser);
        }

        return currentUser;
    }

    @Override
    public void deleteUser(Long idUser) {
        log.debug("deleteUser method called, parameter -> idUser: " + idUser);

        userRepository.deleteById(idUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.debug("getUserByEmail method is called, parameter -> email: " + email);

        return userRepository.findByEmail(email);
    }
}
