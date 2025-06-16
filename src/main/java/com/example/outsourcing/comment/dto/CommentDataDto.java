package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
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

    // 댓글 전체 조회 생성자 (Comment를 CommentDataDto타입으로 변환)
    public static CommentDataDto toDto(Comment comment) {
        return new CommentDataDto(
                comment.getCommentId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
