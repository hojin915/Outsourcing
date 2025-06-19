package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class TaskDoneRatioDto implements TargetIdentifiable {

    private String completionRatio;

    public TaskDoneRatioDto(String completionRatio) {
        this.completionRatio = completionRatio;
    }

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
