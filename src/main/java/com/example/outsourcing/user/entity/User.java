package com.example.outsourcing.user.entity;

import com.example.outsourcing.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String name;
    private UserRole userRole;

    public User(String username, String email, String password, String name, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }
    public User(String username, UserRole userRole) {
        this.username = username;
        this.userRole = userRole;
    }

    // image

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}