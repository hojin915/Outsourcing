package com.example.outsourcing.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PriorityTaskForTargetIdDto {
    private List<TaskByPriorityDto> tasksList;
    private Long targetId;
}
