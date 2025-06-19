package com.example.outsourcing.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class CountOverdueTaskDto {
    private Long countOverdueTask;
    private Long targetId;
}
