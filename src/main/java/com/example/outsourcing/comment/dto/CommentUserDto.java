package com.example.outsourcing.comment.dto;

import com.example.outsourcing.user.entity.User;
import lombok.Getter;

@Getter
public class CommentUserDto {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    public CommentUserDto(Long id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    // user객체를 CommentUserDto타입으로 변환
    public static CommentUserDto toDto(User user) {
        return new CommentUserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }
}
