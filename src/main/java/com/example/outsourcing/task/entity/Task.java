package com.example.outsourcing.task.entity;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    // private Priority priority;

    // private Status status;

    private LocalDate startDate;
    private LocalDate dueDate;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}
