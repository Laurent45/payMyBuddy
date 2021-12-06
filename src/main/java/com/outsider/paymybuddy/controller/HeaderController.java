package com.outsider.paymybuddy.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HeaderController {

    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }
}
