package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.*;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.TransactionRepository;
import com.outsider.paymybuddy.service.ITransactionService;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final IUserService userService;


    @Override
    public Transaction addTransaction(Transaction transaction)
            throws ConstraintErrorException {
        log.info("addTransaction method called, parameter -> transaction: "
                + transaction);

        if (transaction.getAmount() == null
                || transaction.getDate() == null
                || transaction.getUserCreditor() == null
                || transaction.getUserDebtor() == null) {
            throw new ConstraintErrorException("some fields not null are null");
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long idTransaction) {
        log.info("deleteTransaction method called, parameter -> " +
                "idTransaction: " + idTransaction);
        transactionRepository.deleteById(idTransaction);
    }

    @Override
    public Transaction getTransactionById(long idTransaction)
            throws TransactionUnknownException {
        log.info("getTransactionById method is called, parameter -> " +
                "idTransaction: " + idTransaction);

        return transactionRepository.findById(idTransaction)
                .orElseThrow(() -> new TransactionUnknownException("none " +
                        "transaction with id: " + idTransaction));
    }

    @Override
    public List<Transaction> getAllTransactionsOfUser(String emailUser,
                                                      boolean creditorDebtor)
            throws UserUnknownException {
        log.info("getAllTransactionOfUser method called, parameters -> " +
                "emailUser: " + emailUser + "/ creditorDebtor: " + creditorDebtor);

        userService.getUserByEmail(emailUser);

        if (creditorDebtor)
            return transactionRepository.findAllByUserCreditorEmail(emailUser);
        else
            return transactionRepository.findAllByUserDebtorEmail(emailUser);
    }

    @Override
    @Transactional
    public Transaction makeTransaction(String debtorEmail
            , String creditorEmail
            , BigDecimal amountTransaction
            , BigDecimal costTransaction
            , String description) throws UserUnknownException,
            EmailAlreadyUsedException, ConstraintErrorException,
            AmountTransferException {
        log.info(String.format("makeTransaction method called, parameters ->" +
                " debtorEmail: %s / creditorEmail: %s / amountTransaction: " +
                "%s", creditorEmail, debtorEmail, amountTransaction));

        BigDecimal transactionWithCost =
                calculateAmountWithCost(amountTransaction, costTransaction);

        User debtor = userService.getUserByEmail(debtorEmail);
        if (debtor.getBalance().compareTo(transactionWithCost) < 0) {
            throw new AmountTransferException("debtor balance < amount " +
                    "transaction");
        }

        debtor.setBalance(debtor.getBalance().subtract(transactionWithCost));
        userService.updateUser(debtor.getIdUser(), debtor);

        User creditor = userService.getUserByEmail(creditorEmail);
        creditor.setBalance(creditor.getBalance().add(amountTransaction));
        userService.updateUser(creditor.getIdUser(), creditor);

        Transaction transaction = new Transaction(LocalDateTime.now()
                , amountTransaction
                , description
                , costTransaction
                , debtor
                , creditor);

        return addTransaction(transaction);
    }

    private BigDecimal calculateAmountWithCost(BigDecimal amountTransaction
            , BigDecimal costTransaction) {
        return BigDecimal.valueOf(
                amountTransaction.floatValue() * ( 1 + (costTransaction.floatValue() / 100))
        ).setScale(2, RoundingMode.HALF_UP);
    }


}
