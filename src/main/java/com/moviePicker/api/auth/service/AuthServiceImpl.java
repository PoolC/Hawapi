package com.moviePicker.api.auth.service;

import com.moviePicker.api.auth.dto.PasswordResetRequest;
import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.auth.infra.JwtTokenProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final MailService mailService;

    @Override
    public String createAccessToken(String loginId, String password) {
        Member member = memberService.getMemberIfRegistered(loginId, password);
        member.loginAndCheckExpelled();
        return jwtTokenProvider.createToken(member);
    }

    @Transactional
    public void sendEmailAuthorizationToken(Member loginMember) throws Exception {
        String authorizationToken = checkMemberAuthorizedAndCreateAuthorizationToken(loginMember);
        mailService.sendEmailAuthorizationToken(loginMember.getEmail(), authorizationToken);
    }

    @Transactional
    public void checkAuthorizationTokenRequestAndChangeMemberRole(Member loginMember, Optional<String> authorizationToken) {
        checkMemberAuthorized(loginMember);
        String validAuthorizationToken = checkRequestValid(authorizationToken);
        loginMember.checkAuthorizationTokenAndChangeMemberRole(validAuthorizationToken);
    }

    public void sendEmailPasswordResetToken(Optional<String> email) throws Exception {
        String validEmail = email.get();
        String passwordResetToken = CreatePasswordResetToken(validEmail);
        mailService.sendEmailPasswordResetToken(validEmail, passwordResetToken);
    }

    public void checkPasswordResetRequestAndUpdatePassword(Optional<String> passwordResetToken, PasswordResetRequest request) {
        Member member = checkPasswordResetRequest(request);
        String validPasswordResetToken = checkRequestValid(passwordResetToken);
        member.checkPasswordResetTokenAndUpdatePassword(validPasswordResetToken, request);
    }

    private String checkMemberAuthorizedAndCreateAuthorizationToken(Member loginMember) {
        checkMemberAuthorized(loginMember);
        return createAuthorizationToken(loginMember);
    }

    private void checkMemberAuthorized(Member loginMember) {
        checkLogin(loginMember);
        loginMember.checkAuthorized();
    }

    private void checkLogin(Member loginMember) {
        Optional.ofNullable(loginMember)
                .orElseThrow(() -> {
                    throw new UnauthenticatedException("로그인하지 않았습니다.");
                });
    }

    private String createAuthorizationToken(Member member) {
        String token = createToken();
        member.updateAuthorizationToken(token);

        return token;
    }

    private String CreatePasswordResetToken(String email) {
        Member member = memberService.getMemberByEmail(email);
        String passwordResetToken = createToken();
        member.updatePasswordResetToken(passwordResetToken);
        return passwordResetToken;
    }

    private String createToken() {
        return RandomString.make(40);
    }

    private Member checkPasswordResetRequest(PasswordResetRequest request) {
        Member member = memberService.getMemberByEmail(request.getEmail());
        request.checkRequestValid();
        return member;
    }

    private String checkRequestValid(Optional<String> value) {
        return value.orElseThrow(() -> new IllegalArgumentException("요청값이 틀렸습니다."));
    }
}
