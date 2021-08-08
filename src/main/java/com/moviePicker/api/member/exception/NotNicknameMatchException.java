package com.moviePicker.api.member.exception;

public class NotNicknameMatchException extends RuntimeException{

    public NotNicknameMatchException(String message) {
        super(message);
    }
}
