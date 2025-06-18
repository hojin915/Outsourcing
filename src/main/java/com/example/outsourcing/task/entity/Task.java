package com.example.outsourcing.task.entity;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TODO;

    // 변경
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE;

        public boolean canTransitionTo(Status next) {
            return (this == Status.TODO && next == Status.IN_PROGRESS) ||
                    (this == Status.IN_PROGRESS && next == Status.DONE);
        }
    }

    // 테스트코드용 생성자
    public Task(Long id) {
        this.id = id;
    }
}
