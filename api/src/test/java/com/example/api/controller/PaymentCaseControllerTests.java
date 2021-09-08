package com.example.api.controller;

import com.example.api.controller.dto.AmountCurrencyPostDto;
import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.api.controller.dto.PaymentPostDto;
import com.example.domain.error.ResourceNotFoundException;
import com.example.domain.error.ValidationException;
import com.example.domain.models.*;
import com.example.domain.repositories.PaymentCaseRepository;
import com.example.persistence.PaymentCaseRepositoryImpl;
import com.example.persistence.entities.PaymentCaseEntity;
import com.example.persistence.repositories.PaymentCaseJpaRepository;
import org.junit.jupiter.api.Nested;
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

public class PaymentCaseControllerTests {

    private static final UUID emptyCaseId = UUID.randomUUID();
    private static final UUID resolvedCaseId = UUID.randomUUID();

    PaymentCaseJpaRepository paymentCaseJpaRepository = new PaymentCaseJpaRepository() {
        @Override
        public List<PaymentCaseEntity> findAll() {
            return null;
        }

        @Override
        public List<PaymentCaseEntity> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<PaymentCaseEntity> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public List<PaymentCaseEntity> findAllById(Iterable<UUID> iterable) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(UUID uuid) {

        }

        @Override
        public void delete(PaymentCaseEntity paymentCaseEntity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends UUID> iterable) {

        }

        @Override
        public void deleteAll(Iterable<? extends PaymentCaseEntity> iterable) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public <S extends PaymentCaseEntity> List<S> saveAll(Iterable<S> iterable) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends PaymentCaseEntity> S saveAndFlush(S s) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> List<S> saveAllAndFlush(Iterable<S> iterable) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<PaymentCaseEntity> iterable) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<UUID> iterable) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public PaymentCaseEntity getOne(UUID uuid) {
            return null;
        }

        @Override
        public PaymentCaseEntity getById(UUID uuid) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends PaymentCaseEntity> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends PaymentCaseEntity> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends PaymentCaseEntity> S save(S s) {
            return s;
        }

        @Override
        public Optional<PaymentCaseEntity> findById(UUID uuid) {
            if (emptyCaseId.equals(uuid)) {
                return Optional.empty();
            } else if (resolvedCaseId.equals(uuid)) {
                return Optional.of(
                        new PaymentCaseEntity(
                                resolvedCaseId,
                                CaseTypeEnum.DENMARK.toString(),
                                ResolutionEnum.ACCEPT.toString(),
                                "paymentId",
                                BigDecimal.TEN,
                                "EUR"
                        )
                );
            } else {
                return Optional.of(
                        new PaymentCaseEntity(
                                uuid,
                                CaseTypeEnum.DENMARK.toString(),
                                ResolutionEnum.UNRESOLVED.toString(),
                                "paymentId",
                                BigDecimal.TEN,
                                "EUR"
                        )
                );
            }
        }

        @Override
        public boolean existsById(UUID uuid) {
            return false;
        }
    };

    PaymentCaseRepositoryImpl paymentCaseRepository = new PaymentCaseRepositoryImpl(paymentCaseJpaRepository);
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
            paymentCaseController.resolvePaymentCase(UUID.randomUUID(), ResolutionEnum.ACCEPT);
        }

        @Test
        public void test02_emptyCaseId() {
            Throwable t = assertThrows(ResourceNotFoundException.class,
                    () -> paymentCaseController.resolvePaymentCase(emptyCaseId, ResolutionEnum.ACCEPT));
            assertEquals("No payment case found with caseId=" + emptyCaseId, t.getMessage());
        }

        @Test
        public void test03_caseAlreadyResolved() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(resolvedCaseId, ResolutionEnum.ACCEPT));
            assertEquals("Payment case has already been resolved: ACCEPT", t.getMessage());
        }

        @Test
        public void test04_invalidResolution() {
            Throwable t = assertThrows(ValidationException.class,
                    () -> paymentCaseController.resolvePaymentCase(UUID.randomUUID(), ResolutionEnum.UNRESOLVED));
            assertEquals("Payment case resolution can only be ACCEPT or REJECT", t.getMessage());
        }

    }




}
