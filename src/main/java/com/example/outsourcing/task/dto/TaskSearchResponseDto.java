package com.example.outsourcing.task.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class TaskSearchResponseDto implements TargetIdentifiable {
    private List<TaskResponseDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    @Setter
    private Long targetId;

    public static TaskSearchResponseDto fromPage(Page<TaskResponseDto> page) {
        return TaskSearchResponseDto.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}