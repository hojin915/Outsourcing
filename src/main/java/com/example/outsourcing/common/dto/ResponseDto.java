package com.example.outsourcing.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"success", "message", "data", "errorCode", "timestamp"})
public class ResponseDto<T> {

    boolean success;
    private Object message;
    private T data;
    private String timestamp;

    public ResponseDto(Object message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(LocalDateTime.now());
    }

}