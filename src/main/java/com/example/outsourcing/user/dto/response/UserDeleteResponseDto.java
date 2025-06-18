package com.example.outsourcing.user.dto.response;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;

@Getter
public class UserDeleteResponseDto implements TargetIdentifiable {
    private Long targetId;

    public UserDeleteResponseDto(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
