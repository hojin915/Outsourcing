package com.example.outsourcing.task.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    // private Priority priority;

    // private Status status;

    private LocalDate startDate;
    private LocalDate dueDate;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}
