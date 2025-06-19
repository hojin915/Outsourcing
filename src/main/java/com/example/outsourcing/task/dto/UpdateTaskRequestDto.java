package com.example.outsourcing.task.dto;

import com.example.outsourcing.task.entity.Task;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateTaskRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String description;

    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재보다 미래여야 합니다.")
    private LocalDateTime dueDate;

    @NotNull(message = "우선순위 필수입니다.")
    private Task.Priority priority;

    private Task.Status status;

    @NotNull(message = "담당자 ID는 필수입니다.")
    private Long assigneeId;

}