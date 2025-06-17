package com.example.outsourcing.user.entity;

import com.example.outsourcing.comment.entity.Comment;
import com.example.outsourcing.common.entity.SoftDeleteEntity;
import com.example.outsourcing.common.enums.UserRole;
import com.example.outsourcing.task.entity.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends SoftDeleteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String name;
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();


    public User(String username, String email, String password, String name, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }

    // image
}