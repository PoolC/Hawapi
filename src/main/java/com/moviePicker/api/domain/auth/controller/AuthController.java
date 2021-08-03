package com.moviePicker.api.domain.auth.controller;

import com.moviePicker.api.domain.auth.dto.LoginRequest;
import com.moviePicker.api.domain.auth.dto.LoginResponse;
import com.moviePicker.api.domain.auth.dto.PasswordResetRequest;
import com.moviePicker.api.domain.member.domain.Member;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthController {
    public ResponseEntity<LoginResponse> createAccessToken(LoginRequest request);

    public ResponseEntity<Void> sendEmailAuthorizationToken(Member member) throws Exception;

    public ResponseEntity<Void> checkAuthorizationCode(Member member, Optional<String> authorizationToken);

    public ResponseEntity<Void> sendEmailPasswordResetToken(Optional<String> email) throws Exception;

    public ResponseEntity<Void> updatePassword(Optional<String> passwordResetToken, PasswordResetRequest request);
}
