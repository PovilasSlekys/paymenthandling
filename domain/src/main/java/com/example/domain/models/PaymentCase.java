package com.example.domain.models;

import com.example.domain.error.ResourceNotFoundException;
import com.example.domain.error.ValidationException;
import com.example.domain.repositories.PaymentCaseRepository;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

public class PaymentCase {

    @Getter
    private final UUID caseId;

    @Getter
    private final CaseTypeEnum caseType;

    @Getter
    private final Payment payment;

    @Getter
    private ResolutionEnum resolution;

    private PaymentCaseRepository repository;

    public PaymentCase(CaseTypeEnum caseType, Payment payment, PaymentCaseRepository repository) {
        this.caseId = UUID.randomUUID();
        this.caseType = caseType;
        this.payment = new Payment(payment.getPaymentId(), payment.getPaymentAmount());
        this.resolution = ResolutionEnum.UNRESOLVED;
        this.repository = repository;
    }

    public PaymentCase(UUID caseId, CaseTypeEnum caseType, Payment payment, ResolutionEnum resolution) {
        this.caseId = caseId;
        this.caseType = caseType;
        this.payment = new Payment(payment.getPaymentId(), payment.getPaymentAmount());
        this.resolution = resolution;
    }

    public PaymentCase(UUID caseId, PaymentCaseRepository repository) {
        this.repository = repository;
        Optional<PaymentCase> paymentCase = this.repository.findById(caseId);
        if (paymentCase.isEmpty()) {
            throw new ResourceNotFoundException("No payment case found with caseId=" + caseId);
        } else {
            this.caseId = paymentCase.get().getCaseId();
            this.caseType = paymentCase.get().getCaseType();
            Payment payment = paymentCase.get().getPayment();
            this.payment = new Payment(payment.getPaymentId(), payment.getPaymentAmount());
            this.resolution = paymentCase.get().getResolution();
        }
    }

    public UUID save() {
        return repository.save(this);
    }

    public void resolveCase(ResolutionEnum resolution) {
        checkResolution(resolution);
        this.resolution = resolution;
        this.repository.save(this);
    }

    private void checkResolution(ResolutionEnum resolution) {
        if (!ResolutionEnum.UNRESOLVED.equals(this.resolution)) {
            throw new ValidationException("Payment case has already been resolved: " + this.resolution);
        }
        if (!ResolutionEnum.ACCEPT.equals(resolution) && !ResolutionEnum.REJECT.equals(resolution)) {
            throw new ValidationException("Payment case resolution can only be ACCEPT or REJECT");
        }
    }

}
