package com.example.outsourcing.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {

    // 반환 양식에 맞춘 Dto (추후 공통 ResponseDto로 변경 예정)
    private final boolean success;

    private final String message;

    private final CommentDataDto data;

    private final LocalDateTime timestamp;

    // 반환 객체 생성자
    public CommentResponseDto(boolean success, String message, CommentDataDto data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

}

