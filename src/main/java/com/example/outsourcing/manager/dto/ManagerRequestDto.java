package com.example.outsourcing.manager.dto;

import lombok.Getter;

@Getter
public class ManagerRequestDto {
    private Long targetId;

    public ManagerRequestDto(Long targetId) {
        this.targetId = targetId;
    }
}
