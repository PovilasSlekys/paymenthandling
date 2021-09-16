package com.example.api.controller;

import com.example.domain.models.*;
import com.example.domain.repositories.PaymentCaseRepository;
import com.example.persistence.entities.PaymentCaseEntity;
import com.example.persistence.repositories.PaymentCaseJpaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PaymentCaseJpaRepositoryFake implements PaymentCaseJpaRepository {

    @Getter
    private final HashMap<UUID, PaymentCaseEntity> repository = new HashMap<>();

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
        List<S> paymentCaseList = new ArrayList<>();
        for (Map.Entry<UUID, PaymentCaseEntity> entry : repository.entrySet()) {
            PaymentCaseEntity value = entry.getValue();
            if (checkExampleMatch(value, example.getProbe())) {
                paymentCaseList.add((S) value);
            }
        }
        return paymentCaseList;
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
        repository.put(s.getCaseId(), s);
        return s;
    }

    @Override
    public Optional<PaymentCaseEntity> findById(UUID uuid) {
        PaymentCaseEntity paymentCaseEntity = repository.get(uuid);
        if (paymentCaseEntity == null) {
            return Optional.empty();
        } else {
            return Optional.of(paymentCaseEntity);
        }
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    private boolean checkExampleMatch(PaymentCaseEntity value, PaymentCaseEntity example) {
        return value.getResolution().equals(example.getResolution())
                && (example.getCurrency() == null
                    || value.getCurrency().equals(example.getCurrency()));
    }

}
