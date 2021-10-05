package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Long idTransaction;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "cost_100", precision = 6, scale = 2)
    private BigDecimal cost100;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_debtor", referencedColumnName = "id_user")
    private User userDebtor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_creditor", referencedColumnName = "id_user")
    private User userCreditor;

    public Transaction(LocalDateTime date, BigDecimal amount, String description, BigDecimal cost100, User userDebtor, User userCreditor) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.cost100 = cost100;
        this.userDebtor = userDebtor;
        this.userCreditor = userCreditor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return date.equals(that.date)
                && amount.equals(that.amount)
                && Objects.equals(description, that.description)
                && Objects.equals(cost100, that.cost100)
                && userDebtor.equals(that.userDebtor)
                && userCreditor.equals(that.userCreditor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description, cost100);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", date=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", cost100=" + cost100 +
                ", userDebtor=" + userDebtor +
                ", userCreditor=" + userCreditor +
                '}';
    }
}
