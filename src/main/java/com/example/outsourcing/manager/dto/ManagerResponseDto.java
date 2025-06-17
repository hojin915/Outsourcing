package com.example.outsourcing.manager.dto;

import com.example.outsourcing.manager.entity.Manager;
import lombok.Getter;

@Getter
public class ManagerResponseDto {
    Long taskId;
    Long managerId;

    public ManagerResponseDto(Long taskId, Long managerId) {
        this.taskId = taskId;
        this.managerId = managerId;
    }

    public ManagerResponseDto(Manager manager) {
        this.taskId = manager.getTask().getId();
        this.managerId = manager.getUser().getId();
    }
}
