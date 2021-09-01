package com.example.api.controller;

import com.example.api.controller.dto.PaymentCasePostDto;
import com.example.domain.models.PaymentCase;
import com.example.domain.models.PaymentCaseAggregate;
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
@RequestMapping("/aggregate")
@Api(tags = {PaymentCaseAggregateController.CONTROLLER_TAG})
public class PaymentCaseAggregateController {

    public static final String CONTROLLER_TAG = "Payment case aggregate controller";
    private final PaymentCaseRepository repository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public PaymentCaseAggregate getPaymentCaseAggregate() {
        return new PaymentCaseAggregate(repository);
    }
}
