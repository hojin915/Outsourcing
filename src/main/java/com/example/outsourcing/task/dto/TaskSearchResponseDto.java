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
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
    private boolean last;

    @Setter
    private Long targetId;

    public static TaskSearchResponseDto fromPage(Page<TaskResponseDto> page) {
        return TaskSearchResponseDto.builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .build();
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}