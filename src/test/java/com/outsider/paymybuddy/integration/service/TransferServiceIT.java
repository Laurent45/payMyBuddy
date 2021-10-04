package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.AmountTransferException;
import com.outsider.paymybuddy.exception.TransferUnknownException;
import com.outsider.paymybuddy.model.PaymentMethod;
import com.outsider.paymybuddy.model.Transfer;
import com.outsider.paymybuddy.model.TransferType;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.TransferServiceImpl;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class TransferServiceIT {

    @Autowired
    private TransferServiceImpl transferServiceSUT;
    @Autowired
    private UserServiceImpl userService;

    private Transfer transfer;
    private User user;

    @BeforeEach
    void setUp() {
        transfer = null;
        user = null;
    }

    @AfterEach
    void tearDown() {
        if (transfer != null)
            transferServiceSUT.deleteTransferById(transfer.getIdTransfer());
        if (user != null)
            userService.deleteUser(user.getIdUser());
    }

    @Test
    void givenId_whenGetTransferById_thenReturnTransfer()
            throws TransferUnknownException {
        Transfer result = transferServiceSUT.getTransferById(2L);

        assertThat(result).isNotNull();
    }

    @Test
    void givenIdUnknown_whenGetTransferById_throwTransferUnknownException() {
        long idUnknown = 12;
        assertThatThrownBy(() -> transferServiceSUT.getTransferById(idUnknown))
                .isInstanceOf(TransferUnknownException.class);
    }

    @Test
    @Transactional
    void givenTransferAndId_whenUpdateTransfer_thenReturnTransferUpdated()
            throws TransferUnknownException {
        Transfer transferUpdate = new Transfer();
        transferUpdate.setType(TransferType.DEBIT);
        transferUpdate.setPaymentMethod(PaymentMethod.BANK_TRANSFER);

        Transfer result = transferServiceSUT.updateTransfer(3L, transferUpdate);

        assertThat(result.getType()).isEqualTo(TransferType.DEBIT);
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.BANK_TRANSFER);
    }

    /*
        Test of deleting a transfer.
     */
    @Test
    @Transactional
    void givenId_whenDeleteTransferById_thenTransferDeleted()
            throws TransferUnknownException {
        Transfer transfer = transferServiceSUT.getTransferById(2L);
        assertThat(transfer).isNotNull();

        transferServiceSUT.deleteTransferById(2L);
        assertThatThrownBy(() -> transferServiceSUT.getTransferById(2L))
                .isInstanceOf(TransferUnknownException.class);
    }

    /*
        Test about interaction between user and transfer
     */
    @Test
    void givenParameters_whenMakeCreditTransfer_thenReturnTransferAndDBUpdate()
            throws Exception {

        this.user = new User("white"
                , "jojo"
                , "jojowhite@gmail.com"
                , "password");
        userService.addUser(this.user);

        BigDecimal amountTransfer = BigDecimal.valueOf(5634, 2);
        BigDecimal balanceBefore = userService.getUserByEmail(this.user.getEmail())
                .getBalance();

        this.transfer = transferServiceSUT.makeTransfer(this.user.getEmail()
                , TransferType.CREDIT
                , PaymentMethod.BANK_TRANSFER
                , amountTransfer
        );

        BigDecimal balanceAfter = userService.getUserByEmail(this.user.getEmail())
                .getBalance();

        assertThat(this.transfer).isNotNull();
        assertThat(balanceAfter.subtract(amountTransfer)).isEqualTo(balanceBefore);
    }

    @Test
    void givenParameters_whenMakeDebitTransfer_thenReturnTransferAndDBUpdate()
            throws Exception {
        BigDecimal actualBalance = BigDecimal.valueOf(23400, 2);
        BigDecimal amountTransfer = BigDecimal.valueOf(13634, 2);

        this.user = new User("iverson"
                , "allen"
                , "alleniverson@gmail.com"
                , "password");
        userService.addUser(user);
        User updateBalance = new User();
        updateBalance.setBalance(actualBalance);
        userService.updateUser(this.user.getIdUser(), updateBalance);

        this.transfer = transferServiceSUT.makeTransfer(this.user.getEmail(),
                TransferType.DEBIT, PaymentMethod.BANK_TRANSFER,
                amountTransfer);

        BigDecimal result = userService.getUserById(this.user.getIdUser())
                .getBalance();

        assertThat(this.transfer).isNotNull();
        assertThat(result).isEqualTo(actualBalance.subtract(amountTransfer));
    }

    @Test
    void givenBadAmountTransfer_whenMakeTransferDebit_throwAmountTransferException()
            throws Exception {
        BigDecimal amountTransfer = BigDecimal.valueOf(63527, 2);
        this.user = new User("iverson"
                , "allen"
                , "alleniverson@gmail.com"
                , "password");
        userService.addUser(user);

        assertThatThrownBy(() -> transferServiceSUT.makeTransfer(this.user.getEmail(),
                TransferType.DEBIT, PaymentMethod.BANK_TRANSFER,
                amountTransfer))
                .isInstanceOf(AmountTransferException.class);
    }


}
