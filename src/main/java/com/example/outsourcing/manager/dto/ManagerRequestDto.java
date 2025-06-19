package com.example.outsourcing.manager.dto;

import lombok.Getter;

@Getter
public class ManagerRequestDto {
    private Long targetUserId;

    public ManagerRequestDto(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
}
