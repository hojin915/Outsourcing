package com.example.outsourcing.comment.dto;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentListResponseDto implements TargetIdentifiable {

    private List<CommentDataDto> results;

    @Setter
    private Long targetId;


    public static CommentListResponseDto fromPage(Page<Comment> page, Long targetId) {
        List<CommentDataDto> results = page.getContent().stream()
                .map(comment -> CommentDataDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getComment())
                        .taskId(comment.getTask().getId())
                        .userId(comment.getUser().getId())
                        .user(CommentUserDto.toDto(comment.getUser()))
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getUpdatedAt())
                        .targetId(targetId)
                        .build()
                )
                .toList();

        return CommentListResponseDto.builder()
                .results(results)
                .targetId(targetId)
                .build();
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
