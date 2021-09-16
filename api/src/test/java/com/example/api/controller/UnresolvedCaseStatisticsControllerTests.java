package com.example.api.controller;

import com.example.domain.models.AmountCurrency;
import com.example.domain.models.CaseTypeEnum;
import com.example.domain.models.ResolutionEnum;
import com.example.domain.models.UnresolvedCaseStatistics;
import com.example.persistence.PaymentCaseRepositoryImpl;
import com.example.persistence.entities.PaymentCaseEntity;
import com.example.persistence.repositories.PaymentCaseJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UnresolvedCaseStatisticsControllerTests {

    private final PaymentCaseJpaRepositoryFake repositoryFake = new PaymentCaseJpaRepositoryFake();

    PaymentCaseRepositoryImpl paymentCaseRepository = new PaymentCaseRepositoryImpl(repositoryFake);

    UnresolvedCaseStatisticsController unresolvedCaseStatisticsController =
            new UnresolvedCaseStatisticsController(paymentCaseRepository);

    @Test
    public void test01_getUnresolvedCaseStatistics() {
        PaymentCaseEntity paymentCaseEntity1 = new PaymentCaseEntity(
                UUID.randomUUID(),
                CaseTypeEnum.DENMARK.toString(),
                ResolutionEnum.UNRESOLVED.toString(),
                "paymentId",
                BigDecimal.ONE,
                "EUR"
        );

        PaymentCaseEntity paymentCaseEntity2 = new PaymentCaseEntity(
                UUID.randomUUID(),
                CaseTypeEnum.DENMARK.toString(),
                ResolutionEnum.UNRESOLVED.toString(),
                "paymentId2",
                BigDecimal.TEN,
                "DKK"
        );

        PaymentCaseEntity paymentCaseEntity3 = new PaymentCaseEntity(
                UUID.randomUUID(),
                CaseTypeEnum.DENMARK.toString(),
                ResolutionEnum.ACCEPT.toString(),
                "paymentId3",
                BigDecimal.TEN,
                "DKK"
        );

        repositoryFake.getRepository().put(paymentCaseEntity1.getCaseId(), paymentCaseEntity1);
        repositoryFake.getRepository().put(paymentCaseEntity2.getCaseId(), paymentCaseEntity2);
        repositoryFake.getRepository().put(paymentCaseEntity3.getCaseId(), paymentCaseEntity3);

        UnresolvedCaseStatistics unresolvedCaseStatistics = unresolvedCaseStatisticsController.getUnresolvedCaseStatistics();

        assertNotNull(unresolvedCaseStatistics);
        assertEquals(3, repositoryFake.getRepository().size());
        assertEquals(BigDecimal.valueOf(2), unresolvedCaseStatistics.getUnresolvedCases());
        assertNotNull(unresolvedCaseStatistics.getUnresolvedCaseAmounts());
        assertEquals(2, unresolvedCaseStatistics.getUnresolvedCaseAmounts().size());

        Optional<AmountCurrency> eurAmount = unresolvedCaseStatistics.getUnresolvedCaseAmounts().stream()
                .filter(amountCurrency -> "EUR".equals(amountCurrency.getCurrency())).findFirst();
        Optional<AmountCurrency> dkkAmount = unresolvedCaseStatistics.getUnresolvedCaseAmounts().stream()
                .filter(amountCurrency -> "DKK".equals(amountCurrency.getCurrency())).findFirst();

        assertTrue(eurAmount.isPresent());
        assertEquals(BigDecimal.ONE, eurAmount.get().getAmount());

        assertTrue(dkkAmount.isPresent());
        assertEquals(BigDecimal.TEN, dkkAmount.get().getAmount());
    }

}
