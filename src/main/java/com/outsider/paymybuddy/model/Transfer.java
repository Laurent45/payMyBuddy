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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_transfer", nullable = false)
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


}
