package com.example.outsourcing.task.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.user.dto.response.UserSummaryResponseDto;
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
    private Task.Priority priority;
    private Task.Status status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    private Long assigneeId;
    private UserSummaryResponseDto assignee;

    @Override
    public Long getTargetId() {
        return this.id;
    }

}