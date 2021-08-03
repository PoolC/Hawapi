package com.moviePicker.api.domain.auth.exception;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(String message) {
        super(message);
    }
}
