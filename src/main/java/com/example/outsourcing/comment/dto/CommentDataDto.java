package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDataDto implements TargetIdentifiable {

    // 댓글 속성
    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final CommentUserDto user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    private Long targetId;

    // 댓글 생성자
    public CommentDataDto(
            Long id,
            String content,
            Long taskId,
            Long userId,
            CommentUserDto user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long targetId
    ) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}