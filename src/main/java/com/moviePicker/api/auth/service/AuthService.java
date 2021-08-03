package com.moviePicker.api.auth.service;

import com.moviePicker.api.auth.dto.PasswordResetRequest;
import com.moviePicker.api.member.domain.Member;

import java.util.Optional;

public interface AuthService {
    public String createAccessToken(String loginId, String password);

    public void sendEmailAuthorizationToken(Member loginMember) throws Exception;

    public void checkAuthorizationTokenRequestAndChangeMemberRole(Member loginMember, Optional<String> AuthorizationToken);

    public void sendEmailPasswordResetToken(Optional<String> email) throws Exception;

    public void checkPasswordResetRequestAndUpdatePassword(Optional<String> passwordResetToken, PasswordResetRequest request);
}
