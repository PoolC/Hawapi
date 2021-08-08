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
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    private final PasswordHashProvider passwordHashProvider;

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NoSuchElementException(String.format("해당 이메일(%s)을 가진 회원이 존재하지 않습니다.", email)));
    }

    @Override
    public Member getMemberIfRegistered(String email, String password) {
        Member member = getMemberByEmail(email);

        if (!passwordHashProvider.matches(password, member.getPassword()))
            throw new WrongPasswordException("아이디와 비밀번호를 확인해주세요.");

        return member;
    }

    @Override
    public void createMember(MemberCreateRequest request) {
        checkEmailExist(request.getEmail());
        checkNicknameExist(request.getNickname());

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

    @Transactional
    public void updateMember(Member loginMember,MemberUpdateRequest request) {
        checkNicknameExist(request.getNickname());
        checkEmailNotExist(loginMember.getEmail());
        loginMember.ChangePasswordNickname(request);
    }

    @Transactional
    public void deleteMember(String nickname){
        memberRepository.deleteByNickname(nickname);
    }

    @Override
    public void checkEmailExist(String email) {
        if(memberRepository.findByEmail(email).isPresent()){
            throw new DuplicateMemberException("해당 이메일로 이미 회원 등록을 하였습니다.");
        }
    }

    @Override
    public void checkNicknameExist(String nickname){
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new DuplicateMemberException("해당 닉네임의 회원이 이미 존재합니다.");
        }
    }

    private void checkEmailNotExist(String email) {
        if(!memberRepository.findByEmail(email).isPresent()){
            throw new DuplicateMemberException("해당 이메일에 해당하는 회원이 없습니다.");
        }
    }

}