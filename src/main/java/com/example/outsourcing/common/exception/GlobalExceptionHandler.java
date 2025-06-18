package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.ExceptionCode;
import com.example.outsourcing.common.exception.exceptions.NotFoundException;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getErrorResponse(status, ex.getMessage());
    }

    // CustomException 추가, 사용시 enum 코드 또는 enum 코드 + 원하는 메세지
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {
        return getCustomErrorResponse(ex.getExceptionCode());
    }

    // @Valid로 DTO를 검증할 때 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // response에 입력한 내용 순서대로 정리
        List<String> sortlist = Arrays.stream(Objects.requireNonNull(ex.getBindingResult()
                                .getTarget()).getClass()
                        .getDeclaredFields())
                .map(Field::getName)
                .toList();

        // 예외처리 목록 순서대로 정리
        List<FieldError> sortErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparingInt(e -> sortlist.indexOf(e.getField())))
                .toList();

        // 첫번째 예외처리 문구
        String errorMessage = sortErrors.get(0).getDefaultMessage();
        return getErrorResponse(status, errorMessage);
    }

    // 예상하지 못한 모든 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Exception: ", ex); // 전체 스택 트레이스 로그 출력
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException
                && ex.getCause().getMessage().contains("task_user_unique")) {
            HttpStatus status = HttpStatus.CONFLICT;
            return getErrorResponse(status, "이미 존재하는 Task-User 매핑입니다");
        }
        return getErrorResponse(HttpStatus.BAD_REQUEST, "데이터 무결성 오류가 발생했습니다");
    }

    public ResponseEntity<ErrorResponseDto> getErrorResponse(HttpStatus status, String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                message,
                status.getReasonPhrase()
        );
        return new ResponseEntity<>(errorResponseDto, status);
    }

    private ResponseEntity<ErrorResponseDto> getCustomErrorResponse(ExceptionCode code) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(code.getMessage(), code);
        return new ResponseEntity<>(errorResponseDto, code.getHttpStatus());
    }

}