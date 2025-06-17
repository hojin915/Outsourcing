package com.example.outsourcing.dashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDoneRatioDto {

    private String completionRatio;

    public TaskDoneRatioDto(String completionRatio) {
        this.completionRatio = completionRatio;
    }
}
