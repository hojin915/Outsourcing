package com.example.outsourcing.activitylog.dto;

import com.example.outsourcing.activitylog.entity.ActivityLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ActivityLogResponseDto {
    private Long id;
    private LocalDateTime requestTime;
    private Long userId;
    private String activityType;
    private Long targetId;

    public static ActivityLogResponseDto fromEntity(ActivityLog entity) {
        return ActivityLogResponseDto.builder()
                .id(entity.getId())
                .requestTime(entity.getRequestTime())
                .userId(entity.getUserId())
                .activityType(entity.getActivityType())
                .targetId(entity.getTargetId())
                .build();
    }

}
