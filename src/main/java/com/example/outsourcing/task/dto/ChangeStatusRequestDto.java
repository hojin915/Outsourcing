package com.example.outsourcing.task.dto;

import com.example.outsourcing.task.entity.Task;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusRequestDto {

    @NotNull(message = "상태는 필수입니다.")
    private Task.Status status;

    public Task.Status getStatus() {
        return status;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }
}
