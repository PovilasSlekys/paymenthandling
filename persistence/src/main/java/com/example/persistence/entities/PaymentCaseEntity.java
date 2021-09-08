package com.example.persistence.entities;

import com.example.domain.models.CaseTypeEnum;
import com.example.domain.models.Payment;
import com.example.domain.models.PaymentCase;
import com.example.domain.models.ResolutionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "PaymentCase")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCaseEntity {

    @Id
    @Column(length = 36, updatable = false, nullable = false, unique = true)
    @Type(type = "uuid-char")
    private UUID caseId;

    @Column(nullable = false)
    private String caseType;

    @Column(nullable = false)
    private String resolution;

    @Column(nullable = false)
    private String paymentId;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 3, nullable = false)
    private String currency;

    public PaymentCaseEntity(PaymentCase paymentCase) {
        this.caseId = paymentCase.getCaseId();
        this.caseType = mapToString(paymentCase.getCaseType());
        this.resolution = mapToString(paymentCase.getResolution());
        this.paymentId = paymentCase.getPayment().getPaymentId();
        this.amount = paymentCase.getPayment().getPaymentAmount().getAmount();
        this.currency = paymentCase.getPayment().getPaymentAmount().getCurrency();
    }

    private String mapToString(Object enumValue) {
        return enumValue == null ? null : enumValue.toString();
    }
}
