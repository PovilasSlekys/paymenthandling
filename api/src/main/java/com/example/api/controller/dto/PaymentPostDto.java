package com.example.api.controller.dto;

import com.example.domain.models.AmountCurrency;
import com.example.domain.models.CaseTypeEnum;
import com.example.domain.models.Payment;
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
