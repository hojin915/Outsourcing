package com.example.outsourcing.user.dto.response;

import com.example.outsourcing.common.dto.TargetIdentifiable;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto implements TargetIdentifiable {
    private Long id;
    private String username;
    private String email;
    private String name;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    private Long targetId;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.userRole = user.getUserRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    @Override
    public Long getTargetId() {
        return this.id;
    }
}