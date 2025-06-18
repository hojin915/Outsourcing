package com.example.outsourcing.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignupRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "사용자명은 4-20자의 영문/숫자만 허용됩니다")
    @NotBlank(message = "사용자명은 필수입니다")
    private final String username;

    @Email(message = "유효한 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수입니다")
    private final String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상의 영문/숫자/특수문자 조합이어야 합니다")
    @NotBlank(message = "비밀번호는 필수입니다")
    private final String password;

    @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
    @NotBlank(message = "이름은 필수입니다")
    private final String name;

    public UserSignupRequestDto(String username, String email, String password, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}