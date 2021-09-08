package com.example.persistence;

import com.example.domain.models.*;
import com.example.persistence.entities.PaymentCaseEntity;
import com.example.persistence.repositories.PaymentCaseJpaRepository;
import com.example.domain.repositories.PaymentCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PaymentCaseRepositoryImpl implements PaymentCaseRepository {

    private final PaymentCaseJpaRepository repository;

    @Override
    public UUID save(PaymentCase paymentCase) {
        PaymentCaseEntity entity;
        entity = repository.save(new PaymentCaseEntity(paymentCase));
        return entity.getCaseId();
    }

    @Override
    public Optional<PaymentCase> findById(UUID caseId) {
        Optional<PaymentCaseEntity> paymentCaseEntity;
        paymentCaseEntity = repository.findById(caseId);
        if (paymentCaseEntity.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(convertEntityToModel(paymentCaseEntity.get()));
        }
    }

    @Override
    public BigDecimal getUnresolvedCases() {
        PaymentCaseEntity entity = new PaymentCaseEntity();
        entity.setResolution(ResolutionEnum.UNRESOLVED.name());
        return BigDecimal.valueOf(repository.findAll(Example.of(entity)).size());
    }

    @Override
    public List<AmountCurrency> getUnresolvedCaseAmounts() {
        PaymentCaseEntity entity = new PaymentCaseEntity();
        entity.setResolution(ResolutionEnum.UNRESOLVED.name());
        List<AmountCurrency> amountCurrencies = new ArrayList<>();
        List<String> currencyList = getUnresolvedCaseCurrencies();
        for (String currency : currencyList) {
            entity.setCurrency(currency);
            BigDecimal amount = repository.findAll(Example.of(entity)).stream()
                    .map(PaymentCaseEntity::getAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            amountCurrencies.add(new AmountCurrency(amount, currency));
        }
        return amountCurrencies;
    }

    private List<String> getUnresolvedCaseCurrencies() {
        PaymentCaseEntity entity = new PaymentCaseEntity();
        entity.setResolution(ResolutionEnum.UNRESOLVED.name());
        return repository.findAll(Example.of(entity)).stream().map(PaymentCaseEntity::getCurrency).distinct().collect(Collectors.toList());
    }

    private PaymentCase convertEntityToModel(PaymentCaseEntity entity) {
        Payment payment = new Payment(entity.getPaymentId(), new AmountCurrency(entity.getAmount(), entity.getCurrency()));
        return new PaymentCase(
                entity.getCaseId(),
                mapToEnum(CaseTypeEnum.class, entity.getCaseType()),
                payment,
                mapToEnum(ResolutionEnum.class, entity.getResolution()));
    }

    private <T extends Enum<T>> T mapToEnum(Class<T> enumClass, String enumValue) {
        return enumValue == null ? null : Enum.valueOf(enumClass, enumValue);
    }
}
