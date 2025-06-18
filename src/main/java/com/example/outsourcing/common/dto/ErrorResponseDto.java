package com.example.outsourcing.common.dto;

import lombok.Getter;

@Getter
public class ErrorResponseDto extends ResponseDto<Void> {

    private final Object errorCode;

    public ErrorResponseDto(Object message, String errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode;
    }
}