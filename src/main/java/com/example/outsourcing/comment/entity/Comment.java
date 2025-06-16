package com.example.outsourcing.comment.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // 생성일/수정일 추가
    // isDeleted/deletedAt 추가

    public Comment() {}

    // Comment객체 생성자
    public Comment(Task task, User user,String comment) {
        this.task = task;
        this.user = user;
        this.comment = comment;
    }
}