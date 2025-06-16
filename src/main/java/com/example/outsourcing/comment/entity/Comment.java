package com.example.outsourcing.comment.entity;

import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.task.entity.Task;
import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}