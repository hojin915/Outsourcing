package com.example.outsourcing.manager.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.manager.entity.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ManagerResponseDto implements TargetIdentifiable {
    Long taskId;
    Long managerId;

    @Setter
    private Long targetId;

    public ManagerResponseDto(Long taskId, Long managerId) {
        this.taskId = taskId;
        this.managerId = managerId;
        this.targetId = managerId;
    }

    public ManagerResponseDto(Manager manager) {
        this.taskId = manager.getTask().getId();
        this.managerId = manager.getUser().getId();
        this.targetId = manager.getId();
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
