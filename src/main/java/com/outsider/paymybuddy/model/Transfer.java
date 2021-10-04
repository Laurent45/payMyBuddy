package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transfer")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return date.equals(transfer.date)
                && type == transfer.type
                && paymentMethod == transfer.paymentMethod
                && amount.equals(transfer.amount)
                && user.equals(transfer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type, paymentMethod, amount, user);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "idTransfer=" + idTransfer +
                ", date=" + date +
                ", type=" + type +
                ", paymentMethod=" + paymentMethod +
                ", amount=" + amount +
                ", user=" + user +
                '}';
    }
}
