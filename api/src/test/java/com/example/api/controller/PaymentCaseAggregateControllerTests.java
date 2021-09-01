package com.example.api.controller;

import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.domain.models.*;
import com.example.domain.repositories.PaymentCaseRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentCaseAggregateControllerTests {

    PaymentCaseRepository paymentCaseRepository = new PaymentCaseRepository() {
        @Override
        public UUID save(PaymentCase paymentCase) {
            return UUID.randomUUID();
        }

        @Override
        public Optional<PaymentCase> findById(UUID caseId) {
            return Optional.empty();
        }

        @Override
        public BigDecimal getUnresolvedCases() {
            return  BigDecimal.TEN;
        }

        @Override
        public List<AmountCurrency> getUnresolvedCaseAmounts() {
            return Arrays.asList(
                    new AmountCurrency(BigDecimal.ONE, "EUR"),
                    new AmountCurrency(BigDecimal.TEN, "DKK")
            );
        }
    };

    PaymentCaseAggregateController paymentCaseAggregateController =
            new PaymentCaseAggregateController(paymentCaseRepository);

    @Test
    public void test01_getPaymentCaseAggregate() {
        PaymentCaseAggregate paymentCaseAggregate = paymentCaseAggregateController.getPaymentCaseAggregate();
        assertNotNull(paymentCaseAggregate);
        assertEquals(BigDecimal.TEN, paymentCaseAggregate.getUnresolvedCases());
        assertNotNull(paymentCaseAggregate.getUnresolvedCaseAmounts());
        assertEquals(2, paymentCaseAggregate.getUnresolvedCaseAmounts().size());
        assertEquals(BigDecimal.ONE, paymentCaseAggregate.getUnresolvedCaseAmounts().get(0).getAmount());
        assertEquals("EUR", paymentCaseAggregate.getUnresolvedCaseAmounts().get(0).getCurrency());
        assertEquals(BigDecimal.TEN, paymentCaseAggregate.getUnresolvedCaseAmounts().get(1).getAmount());
        assertEquals("DKK", paymentCaseAggregate.getUnresolvedCaseAmounts().get(1).getCurrency());
    }

}
