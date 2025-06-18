package com.example.outsourcing.task.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class TaskListResponseDto implements TargetIdentifiable {
    private List<TaskResponseDto> tasks; // 태스크 목록

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}