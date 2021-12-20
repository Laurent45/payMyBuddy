package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.CustomUserDetails;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.service.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@AllArgsConstructor
public class HeaderController {

    private final ITransactionService transactionService;

    @ModelAttribute("transactions")
    private List<Transaction> getTransactions(Authentication authentication)
            throws UserUnknownException {
        String email = ((CustomUserDetails) authentication.getPrincipal())
                .getUsername();
        return transactionService.getAllTransactionsOfUser(email, false);
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }
}
