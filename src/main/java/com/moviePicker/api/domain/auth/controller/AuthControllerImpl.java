package com.moviePicker.api.domain.auth.controller;

import com.moviePicker.api.domain.auth.dto.LoginRequest;
import com.moviePicker.api.domain.auth.dto.LoginResponse;
import com.moviePicker.api.domain.auth.dto.PasswordResetRequest;
import com.moviePicker.api.domain.auth.service.AuthService;
import com.moviePicker.api.domain.auth.service.JavaMailServiceImpl;
import com.moviePicker.api.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;
    private final JavaMailServiceImpl mailService;

    @Override
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> createAccessToken(@RequestBody LoginRequest request) {
        String accessToken = authService.createAccessToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

    @Override
    @PostMapping(value = "/authorization")
    public ResponseEntity<Void> sendEmailAuthorizationToken(@AuthenticationPrincipal Member loginMember) throws Exception {
        authService.sendEmailAuthorizationToken(loginMember);
        return ResponseEntity.ok().build();
    }
    
    @Override
    @PutMapping(value = "/authorization")
    public ResponseEntity<Void> checkAuthorizationCode(@AuthenticationPrincipal Member loginMember,
                                                       @RequestParam(name = "AuthorizationToken") Optional<String> authorizationToken) {
        authService.checkAuthorizationTokenRequestAndChangeMemberRole(loginMember, authorizationToken);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping(value = "/password-reset")
    public ResponseEntity<Void> sendEmailPasswordResetToken(@RequestParam(name = "Email") Optional<String> email) throws Exception {
        authService.sendEmailPasswordResetToken(email);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping(value = "/password-reset")
    public ResponseEntity<Void> updatePassword(@RequestParam(name = "PasswordResetToken") Optional<String> passwordResetToken,
                                               @RequestBody PasswordResetRequest request) {
        authService.checkPasswordResetRequestAndUpdatePassword(passwordResetToken, request);
        return ResponseEntity.ok().build();
    }


}
