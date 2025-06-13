package com.example.outsourcing.common.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class AopLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestedAt;
    private String ipAddress;
    private String method;
    private String url;

    // private LogActionType ActionType;

    // 생각한 구상, 수정 가능
    // private String targetEntity;
    // private Long targetId;
}
