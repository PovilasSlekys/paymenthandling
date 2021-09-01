package com.example.domain.error;

import com.example.domain.error.ErrorInformation;
import com.example.domain.error.ErrorTypeEnum;
import lombok.Data;

@Data
public class ValidationException extends RuntimeException implements ErrorInformation {

    private final ErrorTypeEnum errorType;
    private final String errorDetails;

    public ValidationException(String message) {
        super(message);
        this.errorType = ErrorTypeEnum.MALFORMED_INPUT;
        this.errorDetails = message;
    }
}
