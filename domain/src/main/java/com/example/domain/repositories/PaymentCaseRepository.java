package com.example.domain.repositories;

import com.example.domain.models.AmountCurrency;
import com.example.domain.models.PaymentCase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentCaseRepository {

    UUID save(PaymentCase paymentCase);

    Optional<PaymentCase> findById(UUID caseId);

    BigDecimal getUnresolvedCases();

    List<AmountCurrency> getUnresolvedCaseAmounts();
}
