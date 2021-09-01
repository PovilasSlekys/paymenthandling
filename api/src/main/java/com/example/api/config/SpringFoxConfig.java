package com.example.api.config;

import com.example.api.controller.PaymentCaseAggregateController;
import com.example.api.controller.PaymentCaseController;
import com.example.domain.error.ErrorInformation;
import com.example.domain.error.ErrorResponse;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    private static final String ERROR_DTO_NAME = "ErrorResponse";

    @Autowired
    public TypeResolver typeResolver;

    @Bean
    public Docket swaggerAPI() { return createDocket().protocols(Sets.newHashSet("http")); }

    public Docket createDocket() {
        String title = "Automatically unresolved payment cases";
        return addActuatorExclusions(new Docket(DocumentationType.SWAGGER_2))
                .globalResponseMessage(RequestMethod.GET,
                        createResponseMessages(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR))
                .globalResponseMessage(RequestMethod.POST,
                        createResponseMessages(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR))
                .globalResponseMessage(RequestMethod.PUT,
                        createResponseMessages(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR))
                .additionalModels(typeResolver.resolve(ErrorResponse.class))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(new Tag(PaymentCaseController.CONTROLLER_TAG, "Responsible for the resolution of unresolved payment cases"),
                        new Tag(PaymentCaseAggregateController.CONTROLLER_TAG, "Responsible for returning aggregate data of unresolved payments"))
                .apiInfo(new ApiInfo(title, "API related to the handling of automatically unresolved payment cases",
                        "1.0", null, null, null, null, new ArrayList<>()));
    }

    private Docket addActuatorExclusions(Docket docket) {
        //Removes all Actuator paths from Swagger
        String[] filterOutPaths = {"error", "actuator"};

        ApiSelectorBuilder builder = docket.select();

        for (String path : filterOutPaths) {
            builder = builder.paths(Predicates.not(PathSelectors.regex("/" + path + "(\\.json)?(/.*)?")));
        }

        return builder.build();
    }

    private List<ResponseMessage> createResponseMessages(HttpStatus... statusCodes) {
        return Arrays.stream(statusCodes)
                .map(this::createResponseMessage)
                .collect(Collectors.toList());
    }

    private ResponseMessage createResponseMessage(HttpStatus statusCode) {
        return new ResponseMessageBuilder().code(statusCode.value())
                .message(statusCode.getReasonPhrase())
                .responseModel(new ModelRef(ERROR_DTO_NAME))
                .build();
    }

}
