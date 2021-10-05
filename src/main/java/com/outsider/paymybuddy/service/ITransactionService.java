package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.exception.*;
import com.outsider.paymybuddy.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {

    /**
     * Create - add a new transaction in DB
     *
     * @param transaction a Transaction object
     * @return a Transaction object saved
     */
    Transaction addTransaction(Transaction transaction) throws ConstraintErrorException;

    /**
     * Delete - delete a transaction in DB
     *
     * @param idTransaction id's transaction
     */
    void deleteTransaction(Long idTransaction);

    /**
     * Read - get a transaction by id
     *
     * @param idTransaction id's transaction
     * @return a transaction object
     */
    Transaction getTransactionById(long idTransaction)
            throws TransactionUnknownException;

    /**
     * Read - get all transaction by id user is a creditor or debtor
     *
     * @param emailUser      email's user
     * @param creditorDebtor true -> creditor / false -> debtor
     * @return a list of transaction
     */
    List<Transaction> getAllTransactionsOfUser(String emailUser
            , boolean creditorDebtor)
            throws UserUnknownException;

    /**
     * Create, Update - add a new transaction in DB and update balance's
     * creditor and balance's debtor
     *
     * @param debtorEmail       email's debtor
     * @param creditorEmail     email's creditor
     * @param amountTransaction amount transaction
     * @param costTransaction   cost transaction
     * @param description       description of transaction
     * @return a Transaction object
     */
    Transaction makeTransaction(String debtorEmail
            , String creditorEmail
            , BigDecimal amountTransaction
            , BigDecimal costTransaction
            , String description)
            throws UserUnknownException
            , EmailAlreadyUsedException
            , ConstraintErrorException
            , AmountTransferException;
}
