package com.example.outsourcing.user.dto.response;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserLoginResponseDto implements TargetIdentifiable {
    private String token;

    @Setter
    private Long targetId;

    public UserLoginResponseDto(String token) {
        this.token = token;
    }

    @Override
    public Long getTargetId() {
        return this.targetId;
    }
}
