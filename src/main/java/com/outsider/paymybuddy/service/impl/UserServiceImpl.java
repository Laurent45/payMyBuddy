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
}
