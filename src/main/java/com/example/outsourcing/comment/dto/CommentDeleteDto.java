package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDeleteDto {

    // 댓글 속성
    private final Long commentId;
    private final Long taskId;
    private final Long userId;
    private final String username;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final boolean isDeleted;
    private final LocalDateTime deletedAt;

    // 댓글 생성자
    public CommentDeleteDto(
            Long commentId,
            Long taskId,
            Long userId,
            String username,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            boolean isDeleted,
            LocalDateTime deletedAt
    ) {
        this.commentId = commentId;
        this.taskId = taskId;
        this.userId = userId;
        this.username = username;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    // 댓글 삭제 생성자 (Comment를 CommentDeleteDto타입으로 변환)
    public static CommentDeleteDto toDto(Comment comment) {
        return new CommentDeleteDto(
                comment.getCommentId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.isDeleted(),
                comment.getDeletedAt()
        );
    }
}
