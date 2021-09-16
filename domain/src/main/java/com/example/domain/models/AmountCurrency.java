package com.example.domain.models;

import com.example.domain.error.ValidationException;
import lombok.Getter;

import java.math.BigDecimal;

public class AmountCurrency {

    @Getter
    private final BigDecimal amount;

    @Getter
    private final String currency;

    public AmountCurrency(BigDecimal amount, String currency) {
        checkAmount(amount);
        checkCurrency(currency);
        this.amount = amount;
        this.currency = currency;
    }

    private void checkAmount(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new ValidationException("Payment must have a positive amount specified");
        }
    }

    private void checkCurrency(String currency) {
        if (currency.length() != 3 ||
                !currency.chars().allMatch(Character::isLetter)) {
            throw new ValidationException("Payment currency must have exactly three letters");
        }
    }
}
