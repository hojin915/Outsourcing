package com.example.outsourcing.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TaskStatusCountsDto {
    private Long todo;
    private Long inProgress;
    private Long done;

    @Builder
    public TaskStatusCountsDto(Long todo, Long inProgress, Long done) {
        this.todo = todo;
        this.inProgress = inProgress;
        this.done = done;
    }
}
