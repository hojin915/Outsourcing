package com.example.outsourcing.activitylog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 발생 시간
    @Column(nullable = false)
    private LocalDateTime requestTime;

    @Column(nullable = false)
    private Long userId;

    // 요청 발생 IP 주소
    @Column(nullable = false)
    private String ipAddress;

    // HTTP 요청 메서드 (GET, POST 등)
    @Column(nullable = false)
    private String requestMethod;

    // 요청된 URL 경로
    @Column(nullable = false)
    private String requestUrl;

    // 활동 유형
//  TASK_CREATED, TASK_UPDATED, TASK_DELETED, TASK_STATUS_CHANGED
//  COMMENT_CREATED, COMMENT_UPDATED, COMMENT_DELETED, USER_LOGGED_IN, USER_LOGGED_OUT
    @Column(nullable = false)
    private String activityType;

//    @Column(nullable = false)
//    private Long targetId;

    @Column(nullable = false)
    private Long executionTimeMs;
//sdf;kalsdfjlka;sdf

}
