package com.example.outsourcing.common.exception;

import com.example.outsourcing.common.dto.ErrorResponseDto;
import com.example.outsourcing.common.exception.exceptions.NotFoundException;
import com.example.outsourcing.common.exception.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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

    public ResponseEntity<ErrorResponseDto> getErrorResponse(HttpStatus status, String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                message,
                status.getReasonPhrase()
        );
        return new ResponseEntity<>(errorResponseDto, status);
    }

}