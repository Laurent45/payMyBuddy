package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.ConstraintErrorException;
import com.outsider.paymybuddy.exception.UserUnknownException;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;
import com.outsider.paymybuddy.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class TransferServiceIT {

    @Autowired
    private TransferServiceImpl transferService;

    /*
        Test of creating a transfer in DB.
     */
    @Test
    void givenTransferAndEmail_whenAddTransfer_thenReturnTransferSaved()
            throws Exception {
        Transfer transfer = new Transfer(LocalDateTime.now(),
                TransferType.DEBIT,
                PaymentMethod.BANK_TRANSFER,
                40.34F);
        String email = "lebronjames@gmail.com";

        Transfer result = transferService.addTransfer(transfer, email);

        assertThat(result).isNotNull();
        assertThat(result.getIdTransfer()).isNotNull();
        assertThat(result.getUser()).isNotNull();
    }

    @Test
    void givenTransferAndEmailUnknown_whenAddTransfer_throwUserUnknown() {
        Transfer transfer = new Transfer(LocalDateTime.now(),
                TransferType.DEBIT,
                PaymentMethod.BANK_TRANSFER,
                40.34F);
        String email = "unknown_email@gmail.com";

        assertThatThrownBy(() -> transferService.addTransfer(transfer, email))
                .isInstanceOf(UserUnknownException.class);
    }

    @Test
    void givenTransferIncompleteAndEmail_whenAddTransfer_throwConstraintViolation() {
        Transfer transfer = new Transfer();
        String email = "lebronjames@gmail.com";
        assertThatThrownBy(() -> transferService.addTransfer(transfer, email))
                .isInstanceOf(ConstraintErrorException.class);
    }

    /*
        Test of reading one or many transfers.
     */
    @Test
    void givenId_whenGetTransferById_thenReturnTransfer() {
        Optional<Transfer> result = transferService.getTransferById(2L);

        assertThat(result).isNotEmpty();
    }

    /*
        Test of updating a transfer.
     */
    @Test
    void givenTransferAndId_whenUpdateTransfer_thenReturnTransferUpdated() {
        Transfer transfer = new Transfer();
        transfer.setType(TransferType.DEBIT);
        transfer.setPaymentMethod(PaymentMethod.BANK_TRANSFER);

        Transfer result = transferService.updateTransfer(3L, transfer);

        assertThat(result.getType()).isEqualTo(TransferType.DEBIT);
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.BANK_TRANSFER);
    }

    /*
        Test of deleting a transfer.
     */
    @Test
    void givenId_whenDeleteTransferById_thenTransferDeleted()
            throws Exception {
        Transfer transfer = new Transfer(LocalDateTime.now(),
                TransferType.DEBIT,
                PaymentMethod.BANK_TRANSFER,
                40.34F);
        String email = "lebronjames@gmail.com";
        Transfer result = transferService.addTransfer(transfer, email);

        transferService.deleteTransferById(result.getIdTransfer());

        assertThat(transferService.getTransferById(result.getIdTransfer())).isEmpty();
    }



}
