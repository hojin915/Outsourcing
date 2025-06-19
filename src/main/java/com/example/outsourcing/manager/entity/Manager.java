package com.example.outsourcing.manager.entity;

import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.task.entity.Task;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "managers",
        uniqueConstraints = {
        @UniqueConstraint(name = "task_user_unique", columnNames = {"task_id", "user_id"})
    }
)
public class Manager extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Manager(Task task, User user) {
        this.task = task;
        this.user = user;
    }
}