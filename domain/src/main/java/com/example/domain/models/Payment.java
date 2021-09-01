package com.example.domain.models;

import com.example.domain.error.ValidationException;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

public class Payment {

    @Getter
    private String paymentId;

    @Getter
    private AmountCurrency paymentAmount;

    public Payment(String paymentId, AmountCurrency paymentAmount) {
        checkPaymentId(paymentId);
        checkAmountCurrency(paymentAmount);
        this.paymentId = paymentId;
        this.paymentAmount = new AmountCurrency(paymentAmount.getAmount(), paymentAmount.getCurrency());
    }

    private void checkPaymentId(String paymentId) {
        if (paymentId == null) {
            throw new ValidationException("Payment ID must be specified");
        }
    }

    private void checkAmountCurrency(AmountCurrency paymentAmount) {
        if (paymentAmount == null) {
            throw new ValidationException("Payment must have an amount specified");
        }
    }

}
