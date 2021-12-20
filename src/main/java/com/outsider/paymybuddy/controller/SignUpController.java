package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.MyMapper;
import com.outsider.paymybuddy.dto.MyMapperImpl;
import com.outsider.paymybuddy.dto.UserDto;
import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("newUser")
    private UserDto setUpNewUser() {
        return new UserDto();
    }

    @GetMapping("/signUp")
    public String getSignUp() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String saveNewUser(@ModelAttribute("newUser") UserDto user)
            throws ConstraintErrorException, EmailAlreadyUsedException {
        MyMapper mapper = new MyMapperImpl();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(mapper.userDtoToUser(user));
        return "redirect:/login";
    }
}
