package com.example.api.controller;

import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.domain.models.*;
import com.example.domain.repositories.PaymentCaseRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentCaseAggregateControllerTests {

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
        public List<PaymentCaseEntity> findAllById(Iterable<UUID> iterable) {
            return null;
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
        public <S extends PaymentCaseEntity> List<S> findAll(Example<S> example) {
            if ("EUR".equals(example.getProbe().getCurrency())) {
                return Arrays.asList((S) new PaymentCaseEntity(
                        UUID.randomUUID(),
                        CaseTypeEnum.DENMARK.toString(),
                        ResolutionEnum.UNRESOLVED.toString(),
                        "paymentId",
                        BigDecimal.ONE,
                        "EUR"
                ));
            } else if ("DKK".equals(example.getProbe().getCurrency())) {
                return Arrays.asList((S) new PaymentCaseEntity(
                        UUID.randomUUID(),
                        CaseTypeEnum.DENMARK.toString(),
                        ResolutionEnum.UNRESOLVED.toString(),
                        "paymentId2",
                        BigDecimal.TEN,
                        "DKK"
                ));
            } else {
                return Arrays.asList((S) new PaymentCaseEntity(
                                UUID.randomUUID(),
                                CaseTypeEnum.DENMARK.toString(),
                                ResolutionEnum.UNRESOLVED.toString(),
                                "paymentId",
                                BigDecimal.ONE,
                                "EUR"
                        ),
                        (S) new PaymentCaseEntity(
                                UUID.randomUUID(),
                                CaseTypeEnum.DENMARK.toString(),
                                ResolutionEnum.UNRESOLVED.toString(),
                                "paymentId2",
                                BigDecimal.TEN,
                                "DKK"
                        )
                        );
            }
        }

        @Override
        public <S extends PaymentCaseEntity> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public Page<PaymentCaseEntity> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends PaymentCaseEntity> S save(S s) {
            return null;
        }

        @Override
        public Optional<PaymentCaseEntity> findById(UUID uuid) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(UUID uuid) {
            return false;
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
        public <S extends PaymentCaseEntity> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
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
    };

    PaymentCaseRepositoryImpl paymentCaseRepository = new PaymentCaseRepositoryImpl(paymentCaseJpaRepository);

    PaymentCaseAggregateController paymentCaseAggregateController =
            new PaymentCaseAggregateController(paymentCaseRepository);

    @Test
    public void test01_getPaymentCaseAggregate() {
        PaymentCaseAggregate paymentCaseAggregate = paymentCaseAggregateController.getPaymentCaseAggregate();
        assertNotNull(paymentCaseAggregate);
        assertEquals(BigDecimal.valueOf(2), paymentCaseAggregate.getUnresolvedCases());
        assertNotNull(paymentCaseAggregate.getUnresolvedCaseAmounts());
        assertEquals(2, paymentCaseAggregate.getUnresolvedCaseAmounts().size());
        assertEquals(BigDecimal.ONE, paymentCaseAggregate.getUnresolvedCaseAmounts().get(0).getAmount());
        assertEquals("EUR", paymentCaseAggregate.getUnresolvedCaseAmounts().get(0).getCurrency());
        assertEquals(BigDecimal.TEN, paymentCaseAggregate.getUnresolvedCaseAmounts().get(1).getAmount());
        assertEquals("DKK", paymentCaseAggregate.getUnresolvedCaseAmounts().get(1).getCurrency());
    }

}
