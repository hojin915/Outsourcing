package com.example.outsourcing.task.dto;

import com.example.outsourcing.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponseDto {

    private Long id;
    private String title;
    private String content;
    private Task.Priority priority;
    private Task.Status status;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private LocalDateTime startDate;

}
