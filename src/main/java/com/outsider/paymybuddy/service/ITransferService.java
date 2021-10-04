package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.exception.*;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;

import java.math.BigDecimal;

public interface ITransferService {


    /**
     * Create - create a transfer
     * @param transfer - a Transfer object to saved in DB
     * @return a Transfer object saved
     */
    Transfer addTransfer(Transfer transfer);

    /**
     * Read - Get a Transfer object by id
     * @param id - id's transfer
     * @return A transfer object
     * @throws TransferUnknownException - id transfer unknown
     */
    Transfer getTransferById(long id) throws TransferUnknownException;

    /**
     * Update - Update a Transfer object
     * @param id - id's transfer
     * @param transfer - a Transfer object with new fields
     * @return a Transfer object updated
     * @throws TransferUnknownException - none transfer in DB with this id
     */
    Transfer updateTransfer(long id, Transfer transfer) throws TransferUnknownException;

    /**
     * Delete - delete a transfer in DB.
     * @param id - id's Transfer object
     */
    void deleteTransferById(Long id);

    /**
     * Make a transfer between the balance of a user and an external account.
     * @param email - email that identify a user
     * @param type - type of transfer
     * @param paymentMethod - method payment
     * @param amount - amount of transfer in a BigDecimal Object
     * @return a transfer object
     */
    Transfer makeTransfer(String email
            , TransferType type
            , PaymentMethod paymentMethod
            , BigDecimal amount)
            throws UserUnknownException
            , ConstraintErrorException
            , AmountTransferException
            , EmailAlreadyUsedException;
}
