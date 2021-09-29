package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.UserRepository;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(long idUser) {
        return userRepository.findById(idUser);
    }

    @Override
    public User updateUser(long id, User user) {
        Optional<User> userToUpdate = userRepository.findById(id);
        if (userToUpdate.isEmpty()) {
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
        userRepository.deleteById(idUser);
    }
}
