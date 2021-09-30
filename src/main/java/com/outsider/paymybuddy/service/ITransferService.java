package com.outsider.paymybuddy.service;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.Transfer;

import java.util.Optional;

public interface ITransferService {
    /**
     * Create - Create a transfer and join to a user.
     * @param transfer - a Transfer object.
     * @param email - an email's user in DB
     * @return - a transfer object saved
     * @throws UserUnknownException - user with this email not found
     * @throws ConstraintErrorException - some fields not null are null
     */
    Transfer addTransfer(Transfer transfer, String email)
            throws UserUnknownException, ConstraintErrorException;

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
}
