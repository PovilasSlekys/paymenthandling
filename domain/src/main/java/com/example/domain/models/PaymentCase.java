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
    private UUID caseId;

    @Getter
    private CaseTypeEnum caseType;

    @Getter
    private Payment payment;

    @Getter
    private ResolutionEnum resolution;

    public PaymentCase(CaseTypeEnum caseType, Payment payment, PaymentCaseRepository repository) {
        checkCaseType(caseType);
        checkPayment(payment);
        this.caseId = UUID.randomUUID();
        this.caseType = caseType;
        this.payment = new Payment(payment.getPaymentId(), payment.getPaymentAmount());
        this.resolution = ResolutionEnum.UNRESOLVED;
        repository.save(this);
    }

    public PaymentCase(UUID caseId, CaseTypeEnum caseType, Payment payment, ResolutionEnum resolution) {
        checkCaseType(caseType);
        checkPayment(payment);
        this.caseId = caseId;
        this.caseType = caseType;
        this.payment = new Payment(payment.getPaymentId(), payment.getPaymentAmount());
        this.resolution = resolution;
    }

    public PaymentCase(UUID caseId, PaymentCaseRepository repository) {
        checkCaseId(caseId);
        Optional<PaymentCase> paymentCase = repository.findById(caseId);
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

    public void resolveCase(ResolutionEnum resolution, PaymentCaseRepository repository) {
        checkResolution(resolution);
        this.resolution = resolution;
        repository.save(this);
    }

    private void checkCaseType(CaseTypeEnum caseType) {
        if (caseType == null) {
            throw new ValidationException("Case type must be specified");
        }
    }

    private void checkPayment(Payment payment) {
        if (payment == null) {
            throw new ValidationException("Payment must be specified");
        }
    }

    private void checkCaseId(UUID caseId) {
        if (caseId == null) {
            throw new ValidationException("Case ID must be specified");
        }
    }

    private void checkResolution(ResolutionEnum resolution) {
        if (!ResolutionEnum.UNRESOLVED.equals(this.resolution)) {
            throw new ValidationException("Payment case has already been resolved: " + this.resolution);
        }
        if (resolution == null) {
            throw new ValidationException("Payment case must have a resolution to be resolved");
        }
        if (!ResolutionEnum.ACCEPT.equals(resolution) && !ResolutionEnum.REJECT.equals(resolution)) {
            throw new ValidationException("Payment case resolution can only be ACCEPT or REJECT");
        }
    }

}
