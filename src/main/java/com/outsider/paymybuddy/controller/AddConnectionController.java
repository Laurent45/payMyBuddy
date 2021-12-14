package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.CustomUserDetails;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"result"})
public class AddConnectionController {

    private final IUserService userService;

    @PostMapping("/connection")
    public String addConnection(
            @RequestParam(value = "connection") String email
            , Authentication authentication
            , Model model
    ) {
        try {
            userService.manageAConnection(((CustomUserDetails) authentication.getPrincipal())
                    .getUsername(), email, true);
        } catch (UserUnknownException e) {
            model.addAttribute("result", -2);
            return "redirect:/home";
        }
        model.addAttribute("result", 2);
        return "redirect:/home";
    }
}
