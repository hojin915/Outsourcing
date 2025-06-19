package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CountOverdueTaskDto implements TargetIdentifiable {
    private Long countOverdueTask;

    public CountOverdueTaskDto(Long countOverdueTask) {
        this.countOverdueTask = countOverdueTask;
    }

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}