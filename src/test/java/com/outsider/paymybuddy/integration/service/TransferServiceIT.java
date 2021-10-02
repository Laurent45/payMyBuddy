package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.TransferServiceImpl;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class TransferServiceIT {

    @Autowired
    private TransferServiceImpl transferService;
    @Autowired
    private UserServiceImpl userService;

    /*
        Test of creating a transfer in DB.
     */

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
        User user = new User("deejay", "yaw", "deejayyaw@gmail.com", "password");
        user = userService.addUser(user);
        Transfer transfer = new Transfer(LocalDateTime.now(),
                TransferType.DEBIT,
                PaymentMethod.BANK_TRANSFER,
                40.34F, user);
        Transfer result = transferService.addTransfer(transfer);

        transferService.deleteTransferById(result.getIdTransfer());

        assertThat(transferService.getTransferById(result.getIdTransfer())).isEmpty();

        userService.deleteUser(user.getIdUser());
    }

    /*
        Test about interaction between user and transfer
     */
    @Test
    void givenEmailAndInformationTransfer_whenMakeTransfer_thenReturnUser()
            throws Exception {
        String email = "rayallen@gmail.com";
        User user = userService.getUserByEmail(email);
        float balance = user.getBalance();
        TransferType type = TransferType.CREDIT;
        PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;
        float amount = 45.00F;

        transferService.makeTransfer(email, type, paymentMethod, amount);

        User userUpdate = userService.getUserByEmail(email);
        float balanceUpdate = userUpdate.getBalance();

        assertThat(balanceUpdate - balance).isEqualTo(amount);

        user.setBalance(0F);
        user.setEmail(null);
        userService.updateUser(user.getIdUser(), user);
    }

    @Test
    void givenInformationTransfer_whenMakeTransfer_thenUpdateBalanceUser()
            throws Exception {
        float actualBalance = 234.00F;
        float amountTransfer = 135.87F;
        User user = new User("deejay", "yaw", "deejayyaw@gmail.com",
                "password", actualBalance);

        user = userService.addUser(user);

        transferService.makeTransfer("deejayyaw@gmail.com",
                TransferType.DEBIT, PaymentMethod.BANK_TRANSFER, amountTransfer);

        User userUpdate = userService.getUserById(user.getIdUser());
        float balanceUpdate = userUpdate.getBalance();

        BigDecimal result =
                new BigDecimal(actualBalance - amountTransfer).setScale(2, RoundingMode.HALF_UP);
        assertThat(balanceUpdate).isEqualTo(result.floatValue());

        userService.deleteUser(user.getIdUser());
    }

    @Test
    void givenBadAmountTransfer_whenMakeTransferDebit_throwAmountTransferException()
            throws Exception {
        float actualBalance = 234.00F;
        float amountTransfer = 356.23F;
        User user = new User("deejay", "yaw", "deejayyaw@gmail.com",
                "password", actualBalance);

        user = userService.addUser(user);

        assertThatThrownBy(() -> transferService.makeTransfer("deejayyaw" +
                        "@gmail.com",
                TransferType.DEBIT, PaymentMethod.BANK_TRANSFER, amountTransfer))
                .isInstanceOf(AmountTransferException.class);

        userService.deleteUser(user.getIdUser());
    }





}
