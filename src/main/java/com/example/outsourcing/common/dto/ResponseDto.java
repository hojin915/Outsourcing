package com.example.outsourcing.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "message", "data", "errorCode", "timestamp"})
public class ResponseDto<T> {

    boolean success;
    private final String message;
    private final T data;
    private final String timestamp;

    public ResponseDto(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().toString();
    }

}