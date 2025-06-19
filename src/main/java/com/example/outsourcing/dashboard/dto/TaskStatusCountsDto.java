package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class TaskStatusCountsDto implements TargetIdentifiable {
    private Long todo;
    private Long inProgress;
    private Long done;

    @Builder
    public TaskStatusCountsDto(Long todo, Long inProgress, Long done) {
        this.todo = todo;
        this.inProgress = inProgress;
        this.done = done;
    }

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
