package com.example.outsourcing.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public UserLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
