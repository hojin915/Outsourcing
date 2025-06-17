package com.example.outsourcing.comment.dto;

import lombok.Getter;

@Getter
public class CommentSearchRequestDto {

    private final String search;

    public CommentSearchRequestDto(String search) {
        this.search = search;
    }
}
