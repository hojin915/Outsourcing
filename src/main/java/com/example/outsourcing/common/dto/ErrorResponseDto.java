package com.example.outsourcing.common.dto;

import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto extends ResponseDto<Void> {

    private final String errorCode;

    public ErrorResponseDto(Object message, String errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode;
    }

    public ErrorResponseDto(Object message, ExceptionCode errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode.toString();
    }

}