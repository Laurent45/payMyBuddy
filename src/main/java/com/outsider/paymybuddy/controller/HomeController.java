package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.TransactionDto;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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

    @ModelAttribute("allUsers")
    private List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/home")
    public String getHome(@SessionAttribute("result") int result
            , HttpSession session
            , Model model
            , Authentication authentication) throws UserUnknownException {
        User user = userService.getUserByEmail(
                ((CustomUserDetails) authentication.getPrincipal())
                        .getUsername()
        );
        model.addAttribute("result", result);
        if (user.getRole().equals("admin"))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);
        session.setAttribute("result", 0);
        return "home";
    }

    @PostMapping("/admin")
    public String updateWallet(
            @RequestParam(value = "user") String email
            , @RequestParam(value = "wallet") BigDecimal wallet
            ) throws UserUnknownException, EmailAlreadyUsedException {
        User user = userService.getUserByEmail(email);
        User userUpdate = new User();
        userUpdate.setBalance(user.getBalance().add(wallet));
        userService.updateUser(user.getIdUser(), userUpdate);
        return "redirect:/home";
    }
}
