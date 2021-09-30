package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer")
@Getter
@Setter
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfer")
    private Long idTransfer;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "transfert_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferType type;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user", referencedColumnName = "id_user")
    private User user;

    public Transfer(LocalDateTime date, TransferType type, PaymentMethod paymentMethod, BigDecimal amount, User user) {
        this.date = date;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.user = user;
    }
}
