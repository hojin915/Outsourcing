package com.example.outsourcing.user.dto.response;

import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignupResponseDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private UserRole userRole;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public UserSignupResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getUserRole();
        // this.createdAt = user.getCreatedAt;
        // this.updatedAt = user.getUpdatedAt;
    }
}
