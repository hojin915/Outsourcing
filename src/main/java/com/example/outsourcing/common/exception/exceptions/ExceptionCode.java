package com.example.outsourcing.common.exception.exceptions;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),

    DELETED_EMAIL(HttpStatus.GONE, "삭제된 이메일입니다"),
    DELETED_USERNAME(HttpStatus.GONE, "삭제된 아이디입니다"),
    DELETED_USER(HttpStatus.GONE, "삭제된 유저입니다"),
    DELETED_TASK(HttpStatus.GONE, "삭제된 일정입니다"),
    DELETED_COMMENT(HttpStatus.GONE, "삭제된 댓글입니다"),

    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    ALREADY_EXISTS_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다"),

    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다");

    // 추가적인 Exception 가능

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
