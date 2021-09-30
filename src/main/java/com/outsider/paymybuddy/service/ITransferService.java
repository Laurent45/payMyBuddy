package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;

import java.util.Optional;

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
     * @return An optional transfer object
     */
    Optional<Transfer> getTransferById(long id);

    /**
     * Update - Update a Transfer object
     * @param id - id's transfer
     * @param transfer - a Transfer object with new fields
     * @return a Transfer object updated
     */
    Transfer updateTransfer(long id, Transfer transfer);

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
     * @param amount - amount of transfer
     */
    void makeTransfer(String email, TransferType type, PaymentMethod paymentMethod, float amount) throws UserUnknownException, ConstraintErrorException, AmountTransferException;
}
