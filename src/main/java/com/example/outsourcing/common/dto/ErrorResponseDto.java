package com.example.outsourcing.common.dto;

import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto extends ResponseDto<Void> {

    private String errorCode;

    public ErrorResponseDto(String message, String errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode;
    }

    public ErrorResponseDto(String message, ExceptionCode errorCode) {
        super(message, null);
        this.success = false;
        this.errorCode = errorCode.toString();
    }

}