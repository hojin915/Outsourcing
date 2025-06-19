package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class CommentDataDto implements TargetIdentifiable {

    // 댓글 속성
    private final Long commentId;
    private final Long taskId;
    private final Long userId;
    private final String username;
    private final String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private final Long targetId;

    // 댓글 생성자
    public CommentDataDto(
            Long commentId,
            Long taskId,
            Long userId,
            String username,
            String comment,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            Long targetId
    ) {
        this.commentId = commentId;
        this.taskId = taskId;
        this.userId = userId;
        this.username = username;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.targetId = targetId;
    }

    // 댓글 전체 조회 생성자 (Comment를 CommentDataDto타입으로 변환)
    public static CommentDataDto toDto(Comment comment, Long targetId) {
        return new CommentDataDto(
                comment.getCommentId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                targetId
        );
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}