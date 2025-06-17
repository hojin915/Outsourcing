package com.example.outsourcing.common.exception;

public class InvalidTaskStateException extends RuntimeException{
    public InvalidTaskStateException(String message) {
        super(message);
    }
}
