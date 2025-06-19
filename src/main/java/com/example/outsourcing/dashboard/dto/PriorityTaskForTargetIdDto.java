package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PriorityTaskForTargetIdDto implements TargetIdentifiable {
    private final List<TaskByPriorityDto> tasksList;

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