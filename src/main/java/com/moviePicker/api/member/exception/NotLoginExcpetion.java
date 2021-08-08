package com.moviePicker.api.member.exception;

public class NotLoginExcpetion extends RuntimeException{

    public NotLoginExcpetion(String message) {
        super(message);
    }
}
