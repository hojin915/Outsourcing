package com.example.outsourcing.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDataDto {

    // 댓글 속성
    private final Long commentId;
    private final Long taskId;
    private final Long userId;
    private final String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 댓글 생성자
    public CommentDataDto(
            Long commentId,
            Long taskId,
            Long userId,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.commentId = commentId;
        this.taskId = taskId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }


}
