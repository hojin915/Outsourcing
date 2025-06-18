package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDataDto {

    // 댓글 속성
    private final Long commentId;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final CommentUserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private final Long targetId;

    // 댓글 생성자
    public CommentDataDto(
            Long commentId,
            String content,
            Long taskId,
            Long userId, CommentUserDto user,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            Long targetId
    ) {
        this.commentId = commentId;
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.user = user;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.targetId = targetId;
    }

    // 댓글 전체 조회 생성자 (Comment를 CommentDataDto타입으로 변환)
    public static CommentDataDto toDto(Comment comment, Long targetId) {
        return new CommentDataDto(
                comment.getCommentId(),
                comment.getComment(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                CommentUserDto.toDto(comment.getUser()),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                targetId
        );
    }
}