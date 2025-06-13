package com.example.outsourcing.user.dto.response;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private String token;

    public UserLoginResponseDto(String token) {
        this.token = token;
    }
}
