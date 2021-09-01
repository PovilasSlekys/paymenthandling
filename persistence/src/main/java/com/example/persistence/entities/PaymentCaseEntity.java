package com.example.persistence.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "PaymentCase")
@Data
public class PaymentCaseEntity {

    @Id
    @Column(length = 36, updatable = false, nullable = false, unique = true)
    @Type(type = "uuid-char")
    private UUID caseId;

    @Column
    private String caseType;

    @Column
    private String resolution;

    @Column
    private String paymentId;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;
}
