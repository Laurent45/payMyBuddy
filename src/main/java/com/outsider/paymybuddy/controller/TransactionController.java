package com.outsider.paymybuddy.controller;

import com.outsider.paymybuddy.dto.TransactionDto;
import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.EmailAlreadyUsedException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.CustomUserDetails;
import com.outsider.paymybuddy.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"result"})
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/transaction")
    public String makeTransfer(
            @ModelAttribute("transaction") TransactionDto t
            , Authentication authentication
            , Model model
    )
            throws UserUnknownException, ConstraintErrorException
            , EmailAlreadyUsedException {
        String emailDebtor = ((CustomUserDetails) authentication.getPrincipal())
                .getUsername();
        try {
            transactionService.makeTransaction(
                    emailDebtor
                    , t.getEmailCreditor()
                    , t.getPrice()
                    , t.getCostTransaction()
                    , t.getDescription()
            );
        } catch (AmountTransferException e) {
            model.addAttribute("result", -1);
            return "redirect:/home";
        }
        model.addAttribute("result", 1);
        return "redirect:/home";
    }
}
