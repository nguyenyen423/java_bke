package com.example.mobilestore.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {

        super(message);
    }
}
