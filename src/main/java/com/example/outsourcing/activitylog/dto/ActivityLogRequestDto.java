package com.example.outsourcing.activitylog.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ActivityLogRequestDto {
    private Long id;
    //시간
    private LocalDateTime requestTime;
    //유저id
    private Long userId;

    // 활동유형
    private String activityType;
    //타겟 id
    private Long targetId;

    private String ipAddress;
    private String requestMethod;
    private String requestUrl;

}


