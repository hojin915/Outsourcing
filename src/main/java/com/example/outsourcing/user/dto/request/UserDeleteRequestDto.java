package com.example.outsourcing.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}