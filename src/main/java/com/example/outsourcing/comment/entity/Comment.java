package com.example.outsourcing.comment.entity;

import com.example.outsourcing.member.entity.Member;
import com.example.outsourcing.task.entity.Task;
import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가
}
