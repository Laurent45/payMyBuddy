package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.TransferRepository;
import com.outsider.paymybuddy.repository.UserRepository;
import com.outsider.paymybuddy.service.ITransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransferServiceImpl implements ITransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    @Override
    public Transfer addTransfer(Transfer transfer) {
        log.debug("addTransfer method called, parameter -> transfer: "
                + transfer);
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
        log.debug("deleteTransferById method called, parameter -> id: " + id);
        transferRepository.deleteById(id);
    }

    @Override
    public void makeTransfer(String email, TransferType type, PaymentMethod paymentMethod, float amount)
            throws UserUnknownException, AmountTransferException {
        log.debug(String.format("makeTransfer method called, parameters -> " +
                "email: %s / transferType: %s / paymentMethod: %s / amount " +
                "%f", email, type.toString(), paymentMethod.toString(), amount));

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.error("not found user with email: " + email);
            throw new UserUnknownException("user not found");
        }
        User user = optionalUser.get();
        float actualBalance = user.getBalance();

        if (type == TransferType.CREDIT) {
            user.setBalance(actualBalance + amount);
        } else if (type == TransferType.DEBIT) {
            if (actualBalance >= amount) {
                user.setBalance(actualBalance - amount);
            } else {
                log.error("the amount transfer is " +
                        "greater than balance user");
                throw new AmountTransferException("the amount transfer is " +
                        "greater than balance user");
            }
        }
        userRepository.save(user);

        Transfer transferRunning = new Transfer(LocalDateTime.now(), type,
                paymentMethod, amount, user);
        addTransfer(transferRunning);
    }
}
