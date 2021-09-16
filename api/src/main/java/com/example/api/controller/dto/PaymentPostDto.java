package com.example.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPostDto {

    private String paymentId;
    private AmountCurrencyPostDto paymentAmount;
}
