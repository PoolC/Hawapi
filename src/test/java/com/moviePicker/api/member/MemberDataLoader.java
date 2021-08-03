package com.moviePicker.api.member;

import com.moviePicker.api.domain.auth.infra.PasswordHashProvider;
import com.moviePicker.api.domain.member.domain.Member;
import com.moviePicker.api.domain.member.domain.MemberRole;
import com.moviePicker.api.domain.member.domain.MemberRoles;
import com.moviePicker.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.moviePicker.api.auth.AuthAcceptanceTest.*;

@Component
@Profile("memberDataLoader")
@RequiredArgsConstructor
public class MemberDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Override
    public void run(String... args) {
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(authorizedEmail)
                        .nickname(authorizedEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(unauthorizedEmail)
                        .nickname(unauthorizedEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(expelledEmail)
                        .nickname(expelledEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.EXPELLED))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(getAuthorizationTokenEmail)
                        .nickname(getAuthorizationTokenEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(authorizationToken)
                        .authorizationTokenValidUntil(LocalDateTime.now().plusDays(1))
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(expiredAuthorizationTokenEmail)
                        .nickname(expiredAuthorizationTokenEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(authorizationToken)
                        .authorizationTokenValidUntil(LocalDateTime.now().minusDays(1))
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(getPasswordResetTokenEmail)
                        .nickname(getPasswordResetTokenEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(passwordResetToken)
                        .passwordResetTokenValidUntil(LocalDateTime.now().plusDays(1))
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(expiredPasswordResetTokenEmail)
                        .nickname(expiredPasswordResetTokenEmail)
                        .passwordHash(passwordHashProvider.encodePassword(password))
                        .passwordResetToken(passwordResetToken)
                        .passwordResetTokenValidUntil(LocalDateTime.now().minusDays(1))
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                        .build());
    }
}
