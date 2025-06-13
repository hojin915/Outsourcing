package com.example.outsourcing.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    //@Pattern(
    //        regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
    //        message = "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다."
    //)
    @NotBlank
    private String password;
    @NotBlank
    private String name;
}