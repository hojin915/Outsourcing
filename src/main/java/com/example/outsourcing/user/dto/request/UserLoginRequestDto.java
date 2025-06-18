package com.example.outsourcing.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {

    @NotBlank(message = "사용자명은 필수입니다")
    private final String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    private final String password;

    public UserLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}