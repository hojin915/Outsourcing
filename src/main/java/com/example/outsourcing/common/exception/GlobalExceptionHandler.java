package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    public ResponseEntity<ErrorResponseDto> getErrorResponse(HttpStatus status, String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                false,
                message + " : " + status.getReasonPhrase(),
                null,
                status.getReasonPhrase(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorResponseDto, status);
    }

}