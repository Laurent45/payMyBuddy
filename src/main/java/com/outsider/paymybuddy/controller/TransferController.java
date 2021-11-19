package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.UserIdDto;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.ITransactionService;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final IUserService userService;
    private final ITransactionService transactionService;

    @ModelAttribute("connections")
    private List<String> getConnections(
            @SessionAttribute("userId") UserIdDto userId)
            throws UserUnknownException {
        User user = userService.getUserById(userId.getIdUser());
        return userService.getEmailsOfUsersConnected(user.getEmail());
    }

    @ModelAttribute("transactions")
    private List<Transaction> getTransactions(
            @SessionAttribute("userId") UserIdDto userId)
            throws UserUnknownException {
        User user = userService.getUserById(userId.getIdUser());
        return transactionService.getAllTransactionsOfUser(user.getEmail(),
                false);
    }

    @GetMapping("/transfer")
    public String getTransfer() {
        return "transfer";
    }
}
