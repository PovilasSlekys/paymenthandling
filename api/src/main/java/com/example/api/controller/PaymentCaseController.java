package com.example.api.controller;

import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.domain.models.AmountCurrency;
import com.example.domain.models.Payment;
import com.example.domain.models.PaymentCase;
import com.example.domain.models.ResolutionEnum;
import com.example.domain.repositories.PaymentCaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentcase")
@Api(tags = {PaymentCaseController.CONTROLLER_TAG})
public class PaymentCaseController {

    public static final String CONTROLLER_TAG = "Payment case controller";
    private final PaymentCaseRepository repository;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public UUID createPaymentCase(
            @ApiParam(value = "paymentCase", required = true) @RequestBody PaymentCasePostDto paymentCaseInput
            ) {
        PaymentCase paymentCase = mapPostDtoToModel(paymentCaseInput);
        return paymentCase.save();
    }

    @GetMapping(value = "/{caseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public PaymentCase getPaymentCase(
            @ApiParam(value = "caseId", required = true) @PathVariable(value = "caseId") UUID caseId
    ) {
        return new PaymentCase(caseId, repository);
    }

    @PutMapping(value = "/{caseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void resolvePaymentCase(
            @ApiParam(value = "caseId", required = true) @PathVariable(value = "caseId") UUID caseId,
            @ApiParam(value = "resolution", required = true) @RequestBody ResolutionEnum resolution
    ) {
        PaymentCase paymentCase = new PaymentCase(caseId, repository);
        paymentCase.resolveCase(resolution);
    }

    private PaymentCase mapPostDtoToModel(PaymentCasePostDto postDto) {
        return new PaymentCase(
                postDto.getCaseType(),
                new Payment(
                        postDto.getPayment().getPaymentId(),
                        new AmountCurrency(
                                postDto.getPayment().getPaymentAmount().getAmount(),
                                postDto.getPayment().getPaymentAmount().getCurrency()
                        )
                ),
                repository
        );
    }
}
