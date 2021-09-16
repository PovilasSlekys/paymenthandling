package com.example.domain.models;

import lombok.Getter;

public class Payment {

    @Getter
    private final String paymentId;

    @Getter
    private final AmountCurrency paymentAmount;

    public Payment(String paymentId, AmountCurrency paymentAmount) {
        this.paymentId = paymentId;
        this.paymentAmount = paymentAmount;
    }

}
