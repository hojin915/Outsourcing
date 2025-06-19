package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class TotalCountsDto implements TargetIdentifiable {
    private final Long totalCount;

    @Setter
    private Long targetId;

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
