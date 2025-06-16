package com.example.outsourcing.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private String comment;

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }
}