package com.example.outsourcing.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@JsonPropertyOrder({"success", "message", "data", "errorCode", "timestamp"})
public class ResponseDto<T> {

    boolean success;
    private final Object message;
    private final T data;
    private final String timestamp;

    public ResponseDto(Object message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(LocalDateTime.now());
    }

}