package com.example.outsourcing.common.dto;

import lombok.Getter;

@Getter
public class ErrorResponseDto extends ResponseDto<Void> {

    private final String errorCode;

    public ErrorResponseDto(String message, String errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode;
    }

}