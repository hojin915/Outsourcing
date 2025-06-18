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
public class CommentSearchResponseDto implements TargetIdentifiable {

    private Long commentId;
    private Long taskId;
    private Long userId;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentSearchResponseDto> searchResults;

    @Setter
    private Long targetId;

    public static CommentSearchResponseDto fromEntity(Comment comment) {
        return CommentSearchResponseDto.builder()
                .commentId(comment.getCommentId())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getUpdatedAt())
                .build();
    }

    public static CommentSearchResponseDto fromList
            (List<CommentSearchResponseDto> searchList, Long loggingTargetId) {
        return CommentSearchResponseDto.builder()
                .searchResults(searchList)
                .targetId(loggingTargetId)
                .build();
    }

    @Override
    public Long getTargetId() {
        return this.targetId != null ? this.targetId : this.userId;
    }
}
