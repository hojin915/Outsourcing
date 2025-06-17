package com.example.outsourcing.dashboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OverdueCountDto {
    private Long overdueCount;

    public OverdueCountDto(Long overdueCount) {
        this.overdueCount = overdueCount;
    }
}
