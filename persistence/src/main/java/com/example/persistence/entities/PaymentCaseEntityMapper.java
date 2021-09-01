package com.example.persistence.entities;

import com.example.domain.models.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentCaseEntityMapper {

    public PaymentCaseEntity convertModelToEntity(PaymentCase model) {
        PaymentCaseEntity entity = new PaymentCaseEntity();
        entity.setCaseId(model.getCaseId());
        entity.setCaseType(mapToString(model.getCaseType()));
        entity.setResolution(mapToString(model.getResolution()));
        entity.setPaymentId(model.getPayment().getPaymentId());
        entity.setAmount(model.getPayment().getPaymentAmount().getAmount());
        entity.setCurrency(model.getPayment().getPaymentAmount().getCurrency());
        return entity;
    }

    public PaymentCase convertEntityToModel(PaymentCaseEntity entity) {
        Payment payment = new Payment(entity.getPaymentId(), new AmountCurrency(entity.getAmount(), entity.getCurrency()));
        return new PaymentCase(
                entity.getCaseId(),
                mapToEnum(CaseTypeEnum.class, entity.getCaseType()),
                payment,
                mapToEnum(ResolutionEnum.class, entity.getResolution()));
    }

    private String mapToString(Object enumValue) {
        return enumValue == null ? null : enumValue.toString();
    }

    private <T extends Enum<T>> T mapToEnum(Class<T> enumClass, String enumValue) {
        return enumValue == null ? null : Enum.valueOf(enumClass, enumValue);
    }
}
