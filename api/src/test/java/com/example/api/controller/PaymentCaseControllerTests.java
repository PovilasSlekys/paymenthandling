package com.example.api.controller;

import com.example.api.controller.dto.AmountCurrencyPostDto;
import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.api.controller.dto.PaymentPostDto;
import com.example.domain.error.ResourceNotFoundException;
import com.example.domain.error.ValidationException;
import com.example.domain.models.CaseTypeEnum;
import com.example.domain.models.PaymentCase;
import com.example.domain.models.ResolutionEnum;
import com.example.persistence.PaymentCaseRepositoryImpl;
import com.example.persistence.entities.PaymentCaseEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentCaseControllerTests {

    private static final UUID emptyCaseId = UUID.randomUUID();
    private static final UUID resolvedCaseId = UUID.randomUUID();
    private static final UUID unresolvedCaseId = UUID.randomUUID();
    private final PaymentCaseJpaRepositoryFake repositoryFake = new PaymentCaseJpaRepositoryFake();

    PaymentCaseRepositoryImpl paymentCaseRepository = new PaymentCaseRepositoryImpl(repositoryFake);
    PaymentCaseController paymentCaseController = new PaymentCaseController(paymentCaseRepository);

    @Nested
    class CreatePaymentCaseTests {

        @Test
        public void test01_success() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.TEN, "EUR")
                            )
                    );
            UUID caseId = paymentCaseController.createPaymentCase(paymentCasePostDto);
            assertNotNull(caseId);
            assertNotNull(repositoryFake.getRepository().get(caseId));
        }

        @Test
        public void test02_invalidPaymentAmount() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.valueOf(-1), "EUR")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment must have a positive amount specified", t.getMessage());
        }

        @Test
        public void test03_invalidPaymentCurrencyLength() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.TEN, "ABCD")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment currency must have exactly three letters", t.getMessage());
        }

        @Test
        public void test04_invalidPaymentCurrencyCharacters() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.TEN, "123")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment currency must have exactly three letters", t.getMessage());
        }

    }

    @Nested
    class GetPaymentCaseTests {

        @Test
        public void test01_success() {
            PaymentCaseEntity paymentCaseEntity =
                    new PaymentCaseEntity(
                            resolvedCaseId,
                            CaseTypeEnum.DENMARK.toString(),
                            ResolutionEnum.ACCEPT.toString(),
                            "paymentId",
                            BigDecimal.TEN,
                            "EUR"
                    );

            repositoryFake.getRepository().put(resolvedCaseId, paymentCaseEntity);

            PaymentCase paymentCase = paymentCaseController.getPaymentCase(resolvedCaseId);
            assertNotNull(paymentCase);
            assertEquals(resolvedCaseId, paymentCase.getCaseId());
            assertEquals(CaseTypeEnum.DENMARK, paymentCase.getCaseType());
            assertNotNull(paymentCase.getPayment());
            assertEquals("paymentId", paymentCase.getPayment().getPaymentId());
            assertNotNull(paymentCase.getPayment().getPaymentAmount());
            assertEquals(BigDecimal.TEN, paymentCase.getPayment().getPaymentAmount().getAmount());
            assertEquals("EUR", paymentCase.getPayment().getPaymentAmount().getCurrency());
            assertEquals(ResolutionEnum.ACCEPT, paymentCase.getResolution());
        }

        @Test
        public void test02_notExistingCaseId() {
            Throwable t = assertThrows(ResourceNotFoundException.class,
                    () -> paymentCaseController.getPaymentCase(emptyCaseId));
            assertEquals("No payment case found with caseId=" + emptyCaseId, t.getMessage());
        }

    }

    @Nested
    class ResolvePaymentCaseTests {

        @Test
        public void test01_success() {
            PaymentCaseEntity paymentCaseEntity =
                    new PaymentCaseEntity(
                            unresolvedCaseId,
                            CaseTypeEnum.DENMARK.toString(),
                            ResolutionEnum.UNRESOLVED.toString(),
                            "paymentId",
                            BigDecimal.TEN,
                            "EUR"
                    );

            repositoryFake.getRepository().put(unresolvedCaseId, paymentCaseEntity);

            paymentCaseController.resolvePaymentCase(unresolvedCaseId, ResolutionEnum.ACCEPT);

            PaymentCaseEntity paymentCase = repositoryFake.getRepository().get(unresolvedCaseId);

            assertNotNull(paymentCase);
            assertEquals(unresolvedCaseId, paymentCase.getCaseId());
            assertEquals(ResolutionEnum.ACCEPT.toString(), paymentCase.getResolution());
        }

        @Test
        public void test02_emptyCaseId() {
            Throwable t = assertThrows(ResourceNotFoundException.class,
                    () -> paymentCaseController.resolvePaymentCase(emptyCaseId, ResolutionEnum.ACCEPT));
            assertEquals("No payment case found with caseId=" + emptyCaseId, t.getMessage());
        }

        @Test
        public void test03_caseAlreadyResolved() {
            PaymentCaseEntity paymentCaseEntity =
                    new PaymentCaseEntity(
                            resolvedCaseId,
                            CaseTypeEnum.DENMARK.toString(),
                            ResolutionEnum.ACCEPT.toString(),
                            "paymentId",
                            BigDecimal.TEN,
                            "EUR"
                    );

            repositoryFake.getRepository().put(resolvedCaseId, paymentCaseEntity);

            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(resolvedCaseId, ResolutionEnum.ACCEPT));
            assertEquals("Payment case has already been resolved: ACCEPT", t.getMessage());
        }

        @Test
        public void test04_invalidResolution() {
            PaymentCaseEntity paymentCaseEntity =
                    new PaymentCaseEntity(
                            unresolvedCaseId,
                            CaseTypeEnum.DENMARK.toString(),
                            ResolutionEnum.UNRESOLVED.toString(),
                            "paymentId",
                            BigDecimal.TEN,
                            "EUR"
                    );

            repositoryFake.getRepository().put(unresolvedCaseId, paymentCaseEntity);

            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(unresolvedCaseId, ResolutionEnum.UNRESOLVED));
            assertEquals("Payment case resolution can only be ACCEPT or REJECT", t.getMessage());
        }

    }




}
