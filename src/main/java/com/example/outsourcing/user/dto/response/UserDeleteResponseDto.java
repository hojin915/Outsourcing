package com.example.outsourcing.user.dto.response;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserDeleteResponseDto implements TargetIdentifiable {
    @Setter
    private Long targetId;

    public UserDeleteResponseDto(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
