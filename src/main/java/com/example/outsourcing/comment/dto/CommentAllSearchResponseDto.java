package com.example.outsourcing.comment.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentAllSearchResponseDto implements TargetIdentifiable {

    private List<CommentDataDto> searchResults;

    @Setter
    private Long targetId;


    public static CommentAllSearchResponseDto fromPage(Page<Comment> page, Long targetId) {
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

        return CommentAllSearchResponseDto.builder()
                .searchResults(results)
                .targetId(targetId)
                .build();
    }

    @Override
    public Long getTargetId() {
        return targetId;
    }
}