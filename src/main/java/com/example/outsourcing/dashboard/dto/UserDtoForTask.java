package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.user.entity.User;
import lombok.Getter;

@Getter
public class UserDtoForTask {

    private Long id;
    private String username;

    public UserDtoForTask(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
