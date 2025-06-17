package com.example.outsourcing.comment.entity;

import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.task.entity.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Setter
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;


    public Comment() {}

    // Comment객체 생성자
    public Comment(Task task, User user,String comment) {
        this.task = task;
        this.user = user;
        this.comment = comment;
    }
}