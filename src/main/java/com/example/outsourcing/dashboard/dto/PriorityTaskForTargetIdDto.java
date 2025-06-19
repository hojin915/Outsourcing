package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PriorityTaskForTargetIdDto implements TargetIdentifiable {
    private List<TaskByPriorityDto> tasksList;

    public PriorityTaskForTargetIdDto(List<TaskByPriorityDto> tasksList) {
        this.tasksList = tasksList;
    }

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
