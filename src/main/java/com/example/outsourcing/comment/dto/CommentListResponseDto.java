package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentListResponseDto implements TargetIdentifiable {

    private Long commentId;
    private Long taskId;
    private Long userId;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentListResponseDto> comments;

    @Setter
    private Long targetId;

    public static CommentListResponseDto fromEntity(Comment comment) {
        return CommentListResponseDto.builder()
                .commentId(comment.getCommentId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getUpdatedAt())
                .build();
    }

    public static CommentListResponseDto fromList(List<CommentListResponseDto> commentList, Long loggingTargetId) {
        return CommentListResponseDto.builder()
                .comments(commentList)
                .targetId(loggingTargetId)
                .build();
    }

    @Override
    public Long getTargetId() {
        return this.targetId != null ? this.targetId : this.userId;
    }
}
