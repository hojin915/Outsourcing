package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.example.outsourcing.common.exception.exceptions.CustomException;
import com.example.outsourcing.common.exception.exceptions.NotFoundException;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HttpStatus status = ex.getExceptionCode().getHttpStatus();
        return getErrorResponse(status, ex.getMessage());
    }

    // @Valid로 DTO를 검증할 때 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) throws JsonProcessingException {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 검증 실패한 메시지를 모두 모아서 리스트로 반환
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> errorDetail = new HashMap<>();
                    errorDetail.put(error.getField(), error.getDefaultMessage());
                    return errorDetail;
                })
                .toList();
        String errorMessage = new ObjectMapper().writeValueAsString(errors);

        return getErrorResponse(status, errorMessage);
    }

    // 예상하지 못한 모든 일반 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Exception: ", ex); // 전체 스택 트레이스 로그 출력
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if(ex.getCause() instanceof ConstraintViolationException
        && ex.getCause().getMessage().contains("task_user_unique")){
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

}