package com.example.outsourcing.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    // image

    //private UserRole userRole;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}
