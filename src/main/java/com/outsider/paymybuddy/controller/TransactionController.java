package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.TransactionDto;
import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/transaction")
    public String makeTransfer(
            @ModelAttribute("transaction") TransactionDto t
            , @SessionAttribute("userEmail") String emailDebtor
            , HttpSession session
    )
            throws UserUnknownException, ConstraintErrorException
            , EmailAlreadyUsedException {
        try {
            transactionService.makeTransaction(
                    emailDebtor
                    , t.getEmailCreditor()
                    , t.getPrice()
                    , t.getCostTransaction()
                    , t.getDescription()
            );
        } catch (AmountTransferException e) {
            session.setAttribute("result", -1);
            return "redirect:/home";
        }
        session.setAttribute("result", 1);
        return "redirect:/home";
    }
}
