package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private final String customMessage;

    public CustomException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
        this.customMessage = code.getMessage();
    }

    public CustomException(ExceptionCode code, String customMessage) {
        super(customMessage);
        this.exceptionCode = code;
        this.customMessage = customMessage;
    }
}
