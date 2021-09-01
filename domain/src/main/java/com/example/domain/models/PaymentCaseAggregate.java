package com.example.domain.models;

import com.example.domain.error.ResourceNotFoundException;
import com.example.domain.error.ValidationException;
import com.example.domain.repositories.PaymentCaseRepository;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentCaseAggregate {

    @Getter
    private BigDecimal unresolvedCases;

    @Getter
    private List<AmountCurrency> unresolvedCaseAmounts;

    public PaymentCaseAggregate(PaymentCaseRepository repository) {
        this.unresolvedCases = repository.getUnresolvedCases();
        this.unresolvedCaseAmounts = repository.getUnresolvedCaseAmounts();
    }

}
