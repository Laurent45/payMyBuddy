package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.UserIdDto;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("userId")
@RequiredArgsConstructor
public class LoginController {

    private final IUserService userService;

    static final String ERROR_MESSAGE = "email and/or password incorrect";

    @ModelAttribute("userId")
    public UserIdDto setUpUserId() {
        return new UserIdDto();
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String home(Model model
            , @ModelAttribute("userId") UserIdDto userId
            , @RequestParam("email") String email
            , @RequestParam("password") String password) {

        User user;
        try {
            user = userService.getUserByEmailAndPassword(email, password);
        } catch (UserUnknownException e) {
            model.addAttribute("errorMsg", ERROR_MESSAGE);
            return "login";
        }
        userId.setIdUser(user.getIdUser());
        return "home";
    }
}
