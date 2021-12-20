package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.TransactionDto;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.CustomUserDetails;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.ITransactionService;
import com.outsider.paymybuddy.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private final IUserService userService;
    private final ITransactionService transactionService;


    @ModelAttribute("connections")
    private List<String> getConnections(Authentication authentication)
            throws UserUnknownException {
        return userService.getEmailsOfUsersConnected(
                ((CustomUserDetails) authentication.getPrincipal())
                        .getUsername());
    }

    @ModelAttribute("transactions")
    private List<Transaction> getTransactions(
            Authentication authentication)
            throws UserUnknownException {
        return transactionService.getAllTransactionsOfUser(
                ((CustomUserDetails) authentication.getPrincipal())
                        .getUsername(),
                false);
    }

    @ModelAttribute("wallet")
    private String getWallet(Authentication authentication)
            throws UserUnknownException {
        User user = userService.getUserByEmail(
                ((CustomUserDetails) authentication.getPrincipal())
                        .getUsername()
        );
        return user.getBalance().toString();
    }

    @ModelAttribute("transaction")
    private TransactionDto setUpTransactionDto() {
        return new TransactionDto();
    }

    @GetMapping("/home")
    public String getHome(@SessionAttribute("result") int result
            , HttpSession session
            , Model model) {
        model.addAttribute("result", result);
        session.setAttribute("result", 0);
        return "home";
    }
}
