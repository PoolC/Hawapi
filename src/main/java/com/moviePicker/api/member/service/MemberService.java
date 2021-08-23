package com.moviePicker.api.member.service;

import com.moviePicker.api.auth.exception.WrongPasswordException;
import com.moviePicker.api.auth.infra.PasswordHashProvider;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.domain.MemberRole;
import com.moviePicker.api.member.domain.MemberRoles;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import com.moviePicker.api.member.exception.DuplicateMemberException;
import com.moviePicker.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NoSuchElementException(String.format("해당 이메일(%s)을 가진 회원이 존재하지 않습니다.", email)));
    }

    public Member getMemberIfRegistered(String email, String password) {
        Member member = getMemberByEmail(email);
        if (!passwordHashProvider.matches(password, member.getPassword()))
            throw new WrongPasswordException("아이디와 비밀번호를 확인해주세요.");
        return member;
    }

    public boolean checkEmailExist(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkNicknameExist(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void create(MemberCreateRequest request) {
        checkCreateRequestValid(request);
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .passwordHash(passwordHashProvider.encodePassword(request.getPassword()))
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .authorizationToken(null)
                        .authorizationTokenValidUntil(null)
                        .reportCount(0)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build()
        );
    }

    public void update(Member member, MemberUpdateRequest request) {
        checkUpdateRequestValid(member, request);
        String encodePassword = passwordHashProvider.encodePassword(request.getPassword());
        member.updateMemberInfo(request, encodePassword);

        //여기서 아이디 설정 안했으니 에러가 떠야함.
        System.out.println("member.getUUID() = " + member.getUUID());
        System.out.println("member.getRoles().hasRole(MemberRole.PUBLIC) = " + member.getRoles().hasRole(MemberRole.PUBLIC));
        memberRepository.saveAndFlush(member);
    }

    public void withdraw(Member member, String nickname) {
        checkWithdrawRequestValid(member, nickname);
        member.withdraw();
    }

    private void checkCreateRequestValid(MemberCreateRequest request) {
        checkDuplicateEmail(request.getEmail());
        checkDuplicateNickname(request.getNickname());
        request.checkPasswordMatches();
    }

    private void checkUpdateRequestValid(Member member, MemberUpdateRequest request) {

        checkDuplicateNickname(request.getNickname());
        request.checkPasswordMatches();
    }

    private void checkWithdrawRequestValid(Member member, String nickname) {

        checkNicknameFormat(nickname);
        checkNicknameMatches(member, nickname);
    }


    private void checkDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateMemberException("이미 가입한 이메일입니다.");
        }
    }

    private void checkDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateMemberException("중복된 닉네임입니다.");
        }
    }

    private void checkNicknameFormat(String nickname) {
        Pattern pattern = Pattern.compile("^[가-힣]*$");
        Matcher matcher = pattern.matcher(nickname);
        if (!matcher.find()) {
            throw new IllegalArgumentException("잘못된 요청(닉네임형식)입니다.");
        }
    }

    private void checkNicknameMatches(Member member, String nickname) {
        member.checkNicknameMatches(nickname);
    }
}