package com.outsider.paymybuddy.service.impl;

import com.outsider.paymybuddy.exception.*;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.repository.TransferRepository;
import com.outsider.paymybuddy.service.ITransferService;
import com.outsider.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransferServiceImpl implements ITransferService {

    private final TransferRepository transferRepository;
    private final IUserService userService;

    @Override
    public Transfer addTransfer(Transfer transfer) {
        log.info("addTransfer method called, parameter -> transfer: "
                + transfer);
        return transferRepository.save(transfer);
    }

    @Override
    public Transfer getTransferById(long id) throws TransferUnknownException {
        log.info("getTransferById method is called, parameter -> id: " + id);

        return transferRepository.findById(id)
                .orElseThrow(() -> new TransferUnknownException("none " +
                        "transfer with id: " + id));
    }

    @Override
    public Transfer updateTransfer(long id, Transfer transfer) throws TransferUnknownException {
        log.info("updateTransfer method is called, parameter -> id: " + id
                + "/ transfer: " + transfer);

        Transfer currentTransfer = getTransferById(id);

        if (currentTransfer != null && transfer != null) {
            if (transfer.getAmount() != null) {
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
        log.info("deleteTransferById method called, parameter -> id: " + id);
        transferRepository.deleteById(id);
    }

    @Override
    public Transfer makeTransfer(String email
            , TransferType type
            , PaymentMethod paymentMethod
            , BigDecimal amount)
            throws UserUnknownException
            , AmountTransferException
            , EmailAlreadyUsedException {
        log.info(String.format("makeTransfer method called, parameters -> " +
                "email: %s / transferType: %s / paymentMethod: %s / amount " +
                "%f", email, type.toString(), paymentMethod.toString(), amount));

        User user = userService.getUserByEmail(email);
        BigDecimal actualBalance = user.getBalance();

        if (type == TransferType.CREDIT) {
            user.setBalance(actualBalance.add(amount));
        } else if (type == TransferType.DEBIT) {

            if (actualBalance.compareTo(amount) >= 0) {
                user.setBalance(actualBalance.subtract(amount));
            } else {
                throw new AmountTransferException("the amount transfer is " +
                        "greater than balance user");
            }

        }

        userService.updateUser(user.getIdUser(), user);
        Transfer transferRunning = new Transfer(LocalDateTime.now(), type,
                paymentMethod, amount, user);

        return addTransfer(transferRunning);
    }
}
