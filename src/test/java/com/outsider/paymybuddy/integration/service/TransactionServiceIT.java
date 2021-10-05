package com.outsider.paymybuddy.integration.service;

import com.outsider.paymybuddy.exception.*;
import com.outsider.paymybuddy.model.Transaction;
import com.outsider.paymybuddy.model.User;
import com.outsider.paymybuddy.service.impl.TransactionServiceImpl;
import com.outsider.paymybuddy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:mysql://localhost" +
        ":3306/payMyBuddy_test?serverTimezone=UTC"})
class TransactionServiceIT {

    @Autowired
    private TransactionServiceImpl transactionServiceSUT;
    @Autowired
    private UserServiceImpl userService;

    private Transaction transaction;
    private User userCreditor;
    private User userDebtor;

    @BeforeEach
    void setUp() {
        transaction = null;
        userCreditor = null;
        userDebtor = null;
    }

    @AfterEach
    void tearDown() {
        if (transaction != null && transaction.getIdTransaction() != null)
            transactionServiceSUT.deleteTransaction(transaction.getIdTransaction());
        if (userCreditor != null && userCreditor.getIdUser() != null)
            userService.deleteUser(userCreditor.getIdUser());
        if (userDebtor != null && userDebtor.getIdUser() != null)
            userService.deleteUser(userDebtor.getIdUser());
    }

    @Test
    void givenTransaction_whenAddTransaction_thenReturnTransactionSaved()
            throws Exception {
        User creditor = userService.getUserById(4L);
        User debtor = userService.getUserById(3L);

        this.transaction = new Transaction(LocalDateTime.now()
        , BigDecimal.valueOf(2435, 2)
        , "test transaction"
        , BigDecimal.valueOf(500, 2)
        , creditor
        , debtor);

        assertThat(this.transaction.getIdTransaction()).isNull();

        transactionServiceSUT.addTransaction(this.transaction);

        assertThat(this.transaction.getIdTransaction()).isNotNull();
    }

    @Test
    void givenTransactionIncomplete_whenAddTransaction_throwConstraintException() {
        this.transaction = new Transaction();
        this.transaction.setDate(LocalDateTime.now());
        this.transaction.setAmount(BigDecimal.valueOf(3));

        assertThatThrownBy(() -> transactionServiceSUT.addTransaction(this.transaction))
                .isInstanceOf(ConstraintErrorException.class);
    }

    @Test
    void givenIdTransaction_whenGetTransactionByID_thenReturnTransaction()
            throws Exception {
        User creditor = userService.getUserById(2L);
        User debtor = userService.getUserById(1L);
        Transaction transaction = new Transaction(LocalDateTime.of(2021, 1,
                13, 14, 30, 3).plusHours(1)
                , BigDecimal.valueOf(5000, 2)
                , "trip money"
                , BigDecimal.valueOf(500, 2)
                , debtor
                , creditor);

        Transaction result = transactionServiceSUT.getTransactionById(7L);

        assertThat(result).isEqualTo(transaction);
    }

    @Test
    void givenIdCreditor_whenGetAllTransactionsById_thenReturnTransactionList()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        List<Transaction> transactionsResult =
                transactionServiceSUT.getAllTransactionsOfUser(emailUser, true);
        assertThat(transactionsResult.size()).isEqualTo(2);
    }

    @Test
    void givenIdDebtor_whenGetAllTransactionsById_thenReturnTransactionList()
            throws UserUnknownException {
        String emailUser = "jamesharden@gmail.com";
        List<Transaction> transactionsResult =
                transactionServiceSUT.getAllTransactionsOfUser(emailUser, false);
        assertThat(transactionsResult.size()).isEqualTo(1);
    }

    @Test
    void givenIdTransaction_whenDeleteTransaction_thenUpdateDB()
            throws Exception {
        User creditor = userService.getUserById(4L);
        User debtor = userService.getUserById(3L);

        Transaction transaction = new Transaction(LocalDateTime.now()
                , BigDecimal.valueOf(2435, 2)
                , "test transaction"
                , BigDecimal.valueOf(500, 2)
                , debtor
                , creditor);

        transactionServiceSUT.addTransaction(transaction);

        transactionServiceSUT.deleteTransaction(transaction.getIdTransaction());
        assertThatThrownBy(() -> transactionServiceSUT
                .getTransactionById(transaction.getIdTransaction()))
                .isInstanceOf(TransactionUnknownException.class);
    }

    @Test
    void givenTwoEmailUser_whenMakeTransaction_thenReturnTransaction()
            throws Exception {
        String debtorEmail = "rudygobert@gmail.com";
        String creditorEmail = "derrickrose@gmail.com";
        BigDecimal costTransaction = BigDecimal.valueOf(500, 2);
        BigDecimal amountTransaction = BigDecimal.valueOf(7667, 2);
        BigDecimal balanceDebtor = BigDecimal.valueOf(45672, 2);
        this.userDebtor = new User("gobert"
                , "rudy"
                , debtorEmail
                , "password");
        this.userDebtor.setBalance(balanceDebtor);
        this.userCreditor = new User("rose"
                , "derrick"
                , creditorEmail
                , "password");

        userService.addUser(this.userDebtor);
        userService.addUser(this.userCreditor);

        this.transaction = transactionServiceSUT.makeTransaction(debtorEmail,
                creditorEmail,
                amountTransaction,
                costTransaction
        , "test transaction");

        assertThat(this.transaction).isNotNull();

        this.userCreditor = userService.getUserByEmail(creditorEmail);
        this.userDebtor = userService.getUserByEmail(debtorEmail);

        assertThat(this.userCreditor.getBalance()).isEqualTo(amountTransaction);
        assertThat(this.userDebtor.getBalance())
                .isEqualTo(BigDecimal.valueOf(37622, 2));
    }

    @Test
    void givenBadAmountTransaction_whenMakeTransaction_throwAmountTransferException()
            throws Exception {
        String debtorEmail = "rudygobert@gmail.com";
        String creditorEmail = "derrickrose@gmail.com";
        BigDecimal costTransaction = BigDecimal.valueOf(500, 2);
        BigDecimal amountTransaction = BigDecimal.valueOf(50000, 2);
        BigDecimal balanceDebtor = BigDecimal.valueOf(45672, 2);
        this.userDebtor = new User("gobert"
                , "rudy"
                , debtorEmail
                , "password");
        this.userDebtor.setBalance(balanceDebtor);
        this.userCreditor = new User("rose"
                , "derrick"
                , creditorEmail
                , "password");

        userService.addUser(this.userDebtor);
        userService.addUser(this.userCreditor);

        assertThatThrownBy(() -> transactionServiceSUT.makeTransaction(
                debtorEmail, creditorEmail, amountTransaction,
                costTransaction, "test_transaction"))
                .isInstanceOf(AmountTransferException.class);
    }


}
