package com.example.api.controller.dto;

import com.example.domain.models.CaseTypeEnum;
import com.example.domain.models.Payment;
import com.example.domain.models.ResolutionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCasePostDto {

    private CaseTypeEnum caseType;
    private PaymentPostDto payment;
}
