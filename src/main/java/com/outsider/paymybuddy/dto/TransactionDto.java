package com.outsider.paymybuddy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class TransactionDto {
    private String emailCreditor;
    private String description;
    private BigDecimal price;
    private BigDecimal costTransaction = BigDecimal.valueOf(500, 2);
}
