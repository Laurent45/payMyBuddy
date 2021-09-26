package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_transaction", nullable = false)
    private Long idTransaction;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "cost_100", precision = 6, scale = 2)
    private BigDecimal cost100;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            , optional = false)
    @JoinColumn(name = "user_debtor", referencedColumnName = "id_user")
    private User userDebtor;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            , optional = false)
    @JoinColumn(name = "user_creditor", referencedColumnName = "id_user")
    private User userCreditor;

}
