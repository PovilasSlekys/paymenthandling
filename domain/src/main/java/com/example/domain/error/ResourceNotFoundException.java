package com.example.domain.error;

import com.example.domain.error.ErrorInformation;
import com.example.domain.error.ErrorTypeEnum;
import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException implements ErrorInformation {

    private final ErrorTypeEnum errorType;
    private final String errorDetails;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorType = ErrorTypeEnum.RESOURCE_NOT_FOUND;
        this.errorDetails = message;
    }
}
