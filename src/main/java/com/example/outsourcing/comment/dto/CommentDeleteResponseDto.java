package com.example.outsourcing.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDeleteResponseDto {
    private final boolean success;

    private final String message;

    private final CommentDeleteDto data;

    private final LocalDateTime timestamp;

    // 반환 객체 생성자
    public CommentDeleteResponseDto(boolean success, String message, CommentDeleteDto data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

}