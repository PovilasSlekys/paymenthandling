package com.example.api.error;

import com.example.domain.error.ErrorInformation;
import com.example.domain.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.example.domain.error.ErrorTypeEnum.UNEXPECTED_ERROR_IN_APPLICATION;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Throwable throwable) {
        String errorCode;
        if (throwable instanceof ErrorInformation) {
            errorCode = ((ErrorInformation)throwable).getErrorType().name();
        } else {
            errorCode = UNEXPECTED_ERROR_IN_APPLICATION.name();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<ErrorResponse> responseEntity =
                new ResponseEntity<ErrorResponse>(
                        new ErrorResponse(errorCode, throwable),
                        httpHeaders,
                        HttpStatus.INTERNAL_SERVER_ERROR);
        ObjectMapper objectMapper = new ObjectMapper();
        boolean a = objectMapper.canSerialize(ErrorResponse.class);
        return responseEntity;
    }

}
