package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("payMyBuddy")
@RequiredArgsConstructor
public class LoginController {

    private final IUserService userService;

    static final String ERROR_MESSAGE = "email and/or password incorrect";

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String home(Model model
            , @RequestParam("email") String email
            , @RequestParam("password") String password) {

        User user;
        try {
            user = userService.getUserByEmailAndPassword(email, password);
        } catch (UserUnknownException e) {
            model.addAttribute("errorMsg", ERROR_MESSAGE);
            return "login";
        }
        model.addAttribute("user", user);
        return "home";
    }
}
