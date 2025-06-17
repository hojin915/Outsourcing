package com.example.outsourcing.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "message", "data", "errorCode", "timestamp"})
public class ResponseDto<T> {

    boolean success;
    private final String message;
    private final T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime timestamp;// 응답 시간

    public ResponseDto(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

}