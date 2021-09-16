package com.example.api.controller.dto;

import com.example.domain.models.CaseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCasePostDto {

    private CaseTypeEnum caseType;
    private PaymentPostDto payment;
}
