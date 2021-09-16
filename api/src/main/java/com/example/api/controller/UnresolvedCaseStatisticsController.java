package com.example.api.controller;

import com.example.domain.models.AmountCurrency;
import com.example.domain.models.UnresolvedCaseStatistics;
import com.example.domain.repositories.PaymentCaseRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@Api(tags = {UnresolvedCaseStatisticsController.CONTROLLER_TAG})
public class UnresolvedCaseStatisticsController {

    public static final String CONTROLLER_TAG = "Unresolved payment case statistics controller";
    private final PaymentCaseRepository repository;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public UnresolvedCaseStatistics getUnresolvedCaseStatistics() {
        UnresolvedCaseStatistics unresolvedCaseStatistics = new UnresolvedCaseStatistics(repository);
        unresolvedCaseStatistics.findUnresolvedCaseStatistics();
        return unresolvedCaseStatistics;
    }
}
