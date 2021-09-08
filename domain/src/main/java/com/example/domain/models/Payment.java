package com.example.domain.models;

import com.example.domain.error.ValidationException;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

public class Payment {

    @Getter
    private final String paymentId;

    @Getter
    private final AmountCurrency paymentAmount;

    public Payment(String paymentId, AmountCurrency paymentAmount) {
        this.paymentId = paymentId;
        this.paymentAmount = new AmountCurrency(paymentAmount.getAmount(), paymentAmount.getCurrency());
    }

}
