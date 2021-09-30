package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.TransferRepository;
import com.outsider.paymybuddy.repository.UserRepository;
import com.outsider.paymybuddy.service.ITransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransferServiceImpl implements ITransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    @Override
    public Transfer addTransfer(Transfer transfer, String email)
            throws UserUnknownException, ConstraintErrorException {
        log.debug("addTransfer method called, parameter -> transfer: "
                + transfer + "/ email: " + email);

        if (transfer.getDate() == null
                || transfer.getType() == null
                || transfer.getPaymentMethod() == null
                || transfer.getAmount() == 0F) {
            log.error("some fields not null are null");
            throw new ConstraintErrorException("some fields not null are null");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.error("user with email: '"+ email +"' not " +
                    "found");
            throw new UserUnknownException("user with email: '"+ email +"' not " +
                    "found");
        }

        transfer.setUser(userOptional.get());

        return transferRepository.save(transfer);
    }

    @Override
    public Optional<Transfer> getTransferById(long id) {
        log.debug("getTransferById method is called, parameter -> id: " + id);
        return transferRepository.findById(id);
    }

    @Override
    public Transfer updateTransfer(long id, Transfer transfer) {
        log.debug("updateTransfer method is called, parameter -> id: " + id
                + "/ transfer: " + transfer);

        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        if (optionalTransfer.isEmpty()) {
            log.debug("none transfer in DB with id: " + id);
            return null;
        }

        Transfer currentTransfer = optionalTransfer.get();
        if (transfer != null) {
            if (transfer.getAmount() != 0) {
                currentTransfer.setAmount(transfer.getAmount());
            }
            if (transfer.getDate() != null) {
                currentTransfer.setDate(transfer.getDate());
            }
            if (transfer.getPaymentMethod() != null) {
                currentTransfer.setPaymentMethod(transfer.getPaymentMethod());
            }
            if (transfer.getType() != null) {
                currentTransfer.setType(transfer.getType());
            }
            return transferRepository.save(currentTransfer);

        }

        return currentTransfer;
    }

    @Override
    public void deleteTransferById(Long id) {
        log.debug("deleteTransferById method called, paramter -> id: " + id);
        transferRepository.deleteById(id);
    }
}
