package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.TransactionDto;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.service.impl.TransactionServiceImpl;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@AllArgsConstructor
public class HeaderController {

    private final UserServiceImpl userService;
    private final TransactionServiceImpl transactionService;

    @ModelAttribute("connections")
    private List<String> getConnections(
            @SessionAttribute("userEmail") String email)
            throws UserUnknownException {
        return userService.getEmailsOfUsersConnected(email);
    }

    @ModelAttribute("transactions")
    private List<Transaction> getTransactions(
            @SessionAttribute("userEmail") String email)
            throws UserUnknownException {
        return transactionService.getAllTransactionsOfUser(email,
                false);
    }

    @ModelAttribute("transaction")
    private TransactionDto getPrice() {
        return new TransactionDto();
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
