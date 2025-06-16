package com.example.outsourcing.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private final boolean success;
    private final String message;
    private final String data;
    private final String errorCode;
    private final String timestamp;

}