package com.example.outsourcing.common.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // 400
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다"),
    COMMENT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "내용을 입력해야합니다"),
    COMMENT_UPDATED_BAD_REQUEST(HttpStatus.BAD_REQUEST, "기존 내용과 동일한 내용입니다"),
    COMMENT_AUTHOR_MISMATCH(HttpStatus.BAD_REQUEST, "작성자가 아닙니다"),
    TASK_COMMENT_MISMATCH(HttpStatus.BAD_REQUEST, "해당 태스크에 존재하지 않는 댓글입니다"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다"),

    UNSUPPORTED_JWT_TOKENS(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰 입니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 토큰입니다."),

    // 401
    UNAUTHORIZED_API_REQUEST(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),

    // 403
    NOT_AUTHOR(HttpStatus.FORBIDDEN, "작성자가 아닙니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "담당자를 찾을 수 없습니다"),

    // 409
    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    ALREADY_EXISTS_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다"),
    ALREADY_EXISTS_MANAGER(HttpStatus.CONFLICT, "이미 존재하는 매니저입니다"),

    // 410
    DELETED_EMAIL(HttpStatus.GONE, "삭제된 이메일입니다"),
    DELETED_USERNAME(HttpStatus.GONE, "삭제된 아이디입니다"),
    DELETED_USER(HttpStatus.GONE, "삭제된 유저입니다"),
    DELETED_TASK(HttpStatus.GONE, "삭제된 일정입니다"),
    DELETED_COMMENT(HttpStatus.GONE, "삭제된 댓글입니다"),
    DELETED_MANAGER(HttpStatus.GONE, "삭제된 매니저입니다");

    // 추가적인 Exception 가능

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}