package com.example.outsourcing.task.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.task.entity.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponseDto implements TargetIdentifiable {

    private Long id;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    private Task.Priority priority;
    private Task.Status status;

    private Long assigneeId;
    private AssigneeDto assignee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Override
    public Long getTargetId() {
        return this.id;
    }

}