package com.example.domain.models;

import com.example.domain.repositories.PaymentCaseRepository;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UnresolvedCaseStatistics {

    @Getter
    private BigDecimal unresolvedCases;

    @Getter
    private List<AmountCurrency> unresolvedCaseAmounts;

    private final PaymentCaseRepository repository;

    public UnresolvedCaseStatistics(PaymentCaseRepository repository) {
        this.repository = repository;
        this.unresolvedCases = BigDecimal.ZERO;
        this.unresolvedCaseAmounts = new ArrayList<>();
    }

    public void findUnresolvedCaseStatistics() {
        this.unresolvedCases = repository.getUnresolvedCases();
        this.unresolvedCaseAmounts = repository.getUnresolvedCaseAmounts();
    }

}
