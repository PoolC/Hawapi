package com.moviePicker.api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PasswordResetRequest {
    private final String email;
    private final String passwordRestToken;
    private final String password;
    private final String passwordCheck;

    @JsonCreator
    @Builder
    public PasswordResetRequest(String email, String passwordRestToken, String password, String passwordCheck) {
        this.email = email;
        this.passwordRestToken = passwordRestToken;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }
}
