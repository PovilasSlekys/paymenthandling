package com.example.api.controller;

import com.example.api.controller.dto.AmountCurrencyPostDto;
import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.api.controller.dto.PaymentPostDto;
import com.example.domain.error.ResourceNotFoundException;
import com.example.domain.error.ValidationException;
import com.example.domain.models.*;
import com.example.domain.repositories.PaymentCaseRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentCaseControllerTests {

    private static final UUID emptyCaseId = UUID.randomUUID();
    private static final UUID resolvedCaseId = UUID.randomUUID();

    PaymentCaseRepository paymentCaseRepository = new PaymentCaseRepository() {
        @Override
        public UUID save(PaymentCase paymentCase) {
            return UUID.randomUUID();
        }

        @Override
        public Optional<PaymentCase> findById(UUID caseId) {
            if (emptyCaseId.equals(caseId)) {
                return Optional.empty();
            } else if (resolvedCaseId.equals(caseId)) {
                return Optional.of(
                        new PaymentCase(
                                resolvedCaseId,
                                CaseTypeEnum.DENMARK,
                                new Payment(
                                        "paymentId",
                                        new AmountCurrency(BigDecimal.TEN, "EUR")
                                ),
                                ResolutionEnum.ACCEPT
                        )
                );
            } else {
                return Optional.of(
                        new PaymentCase(
                                CaseTypeEnum.DENMARK,
                                new Payment(
                                        "paymentId",
                                        new AmountCurrency(BigDecimal.TEN, "EUR")
                                ),
                                paymentCaseRepository
                        )
                );
            }
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
        }

        @Test
        public void test02_emptyCaseType() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            null,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.TEN, "EUR")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Case type must be specified", t.getMessage());
        }

        @Test
        public void test03_emptyPayment() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            null
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment must be specified", t.getMessage());
        }

        @Test
        public void test04_emptyPaymentId() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    null,
                                    new AmountCurrencyPostDto(BigDecimal.TEN, "EUR")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment ID must be specified", t.getMessage());
        }

        @Test
        public void test05_emptyPaymentAmount() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    null
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment must have an amount specified", t.getMessage());
        }

        @Test
        public void test06_emptyPaymentAmount() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(null, "EUR")
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment amount must have a value specified", t.getMessage());
        }

        @Test
        public void test06_invalidPaymentAmount() {
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
        public void test07_emptyPaymentCurrency() {
            PaymentCasePostDto paymentCasePostDto =
                    new PaymentCasePostDto(
                            CaseTypeEnum.DENMARK,
                            new PaymentPostDto(
                                    "paymentId",
                                    new AmountCurrencyPostDto(BigDecimal.TEN, null)
                            )
                    );
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.createPaymentCase(paymentCasePostDto));
            assertEquals("Payment must have a currency specified", t.getMessage());
        }

        @Test
        public void test08_invalidPaymentCurrencyLength() {
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
        public void test09_invalidPaymentCurrencyCharacters() {
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
            PaymentCase paymentCase = paymentCaseController.getPaymentCase(UUID.randomUUID());
            assertNotNull(paymentCase);
            assertNotNull(paymentCase.getCaseId());
            assertEquals(CaseTypeEnum.DENMARK, paymentCase.getCaseType());
            assertNotNull(paymentCase.getPayment());
            assertNotNull(paymentCase.getPayment().getPaymentId());
            assertNotNull(paymentCase.getPayment().getPaymentAmount());
            assertNotNull(paymentCase.getPayment().getPaymentAmount().getAmount());
            assertNotNull(paymentCase.getPayment().getPaymentAmount().getCurrency());
            assertEquals(ResolutionEnum.UNRESOLVED, paymentCase.getResolution());
        }

        @Test
        public void test02_invalidCaseId() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.getPaymentCase(null));
            assertEquals("Case ID must be specified", t.getMessage());
        }

        @Test
        public void test03_notExistingCaseId() {
            Throwable t = assertThrows(ResourceNotFoundException.class,
                    () -> paymentCaseController.getPaymentCase(emptyCaseId));
            assertEquals("No payment case found with caseId=" + emptyCaseId, t.getMessage());
        }

    }

    @Nested
    class ResolvePaymentCaseTests {

        @Test
        public void test01_success() {
            paymentCaseController.resolvePaymentCase(UUID.randomUUID(), ResolutionEnum.ACCEPT);
        }

        @Test
        public void test02_invalidCaseId() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(null, ResolutionEnum.ACCEPT));
            assertEquals("Case ID must be specified", t.getMessage());
        }

        @Test
        public void test03_emptyCaseId() {
            Throwable t = assertThrows(ResourceNotFoundException.class,
                    () -> paymentCaseController.resolvePaymentCase(emptyCaseId, ResolutionEnum.ACCEPT));
            assertEquals("No payment case found with caseId=" + emptyCaseId, t.getMessage());
        }

        @Test
        public void test04_caseAlreadyResolved() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(resolvedCaseId, ResolutionEnum.ACCEPT));
            assertEquals("Payment case has already been resolved: ACCEPT", t.getMessage());
        }

        @Test
        public void test05_emptyResolution() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(UUID.randomUUID(), null));
            assertEquals("Payment case must have a resolution to be resolved", t.getMessage());
        }

        @Test
        public void test06_invalidResolution() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(UUID.randomUUID(), ResolutionEnum.UNRESOLVED));
            assertEquals("Payment case resolution can only be ACCEPT or REJECT", t.getMessage());
        }

    }




}
