package com.example.domain.models;

import com.example.domain.error.ValidationException;
import lombok.Getter;

import java.math.BigDecimal;

public class AmountCurrency {

    @Getter
    private BigDecimal amount;

    @Getter
    private String currency;

    public AmountCurrency(BigDecimal amount, String currency) {
        checkAmount(amount);
        checkCurrency(currency);
        this.amount = amount;
        this.currency = currency;
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null) {
            throw new ValidationException("Payment amount must have a value specified");
        }
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new ValidationException("Payment must have a positive amount specified");
        }
    }

    private void checkCurrency(String currency) {
        if (currency == null) {
            throw new ValidationException("Payment must have a currency specified");
        }
        if (currency.length() != 3 ||
                !currency.chars().allMatch(Character::isLetter)) {
            throw new ValidationException("Payment currency must have exactly three letters");
        }
    }
}
