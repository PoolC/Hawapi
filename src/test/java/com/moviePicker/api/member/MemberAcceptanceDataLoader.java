package com.moviePicker.api.member;

import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("memberAcceptanceDataLoader")
@RequiredArgsConstructor
public class MemberAcceptanceDataLoader implements CommandLineRunner{
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    public static String defaultEmail ="existingEmail@gmail.com", toBeWithdrawEmail="toBeWithdrawEmail@gmail.com",
        defaultNickname ="존재하는닉네임",toBeWithdrawNickname="삭제될회원닉네임",
        defaultPassword ="password123!",toBeWithdrawPassword="tobewithdraw123!";


    @Override
    public void run(String... args){
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(defaultEmail)
                        .nickname(defaultNickname)
                        .passwordHash(passwordHashProvider.encodePassword(defaultPassword))
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
                    .email(toBeWithdrawEmail)
                    .nickname(toBeWithdrawNickname)
                    .passwordHash(passwordHashProvider.encodePassword(toBeWithdrawPassword))
                    .passwordResetToken(null)
                    .passwordResetTokenValidUntil(null)
                    .authorizationToken(null)
                    .authorizationTokenValidUntil(null)
                    .reportCount(0)
                    .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                    .build());
    }

}