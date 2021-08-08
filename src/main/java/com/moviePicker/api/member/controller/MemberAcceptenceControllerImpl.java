package com.moviePicker.api.member.controller;

import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.auth.exception.WrongTokenException;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import com.moviePicker.api.member.exception.DuplicateMemberException;
import com.moviePicker.api.member.exception.NotLoginExcpetion;
import com.moviePicker.api.member.exception.NotNicknameMatchException;
import com.moviePicker.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberAcceptenceControllerImpl implements MemberAcceptenceControlller{

    private final MemberService memberService;

    @Override
    @PostMapping()
    public ResponseEntity<LoginResponse> createMember(@RequestBody MemberCreateRequest request) {
        request.checkColumn();
        request.checkPasswordSame();
        request.checkEmailFormat();
        memberService.createMember(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/me")
    public ResponseEntity<LoginResponse> updateMember(@AuthenticationPrincipal Member loginMember,@RequestBody MemberUpdateRequest request) throws NotLoginExcpetion {
        checkLogin(loginMember);
        request.checkPasswordFormat();
        memberService.updateMember(loginMember,request);
        return ResponseEntity.ok().build();
    }

    private void checkLogin(Member loginMember) {
        Optional.ofNullable(loginMember).
                orElseThrow(()-> {
                    throw new NotLoginExcpetion("로그인하지 않았습니다.");
                });
    }

    @Override
    @PutMapping("/{nickname}")
    public ResponseEntity<LoginResponse> withdrawMember(@AuthenticationPrincipal Member loginMember,@PathVariable(name = "nickname") String nickname){
        checkNicknameFormat(nickname);
        loginMember.checkNickname(nickname);
        memberService.deleteMember(nickname);
        return ResponseEntity.ok().build();
    }

    private void checkNicknameFormat(String nickname){
        if(!nickname.matches("^[ㄱ-ㅎ가-힣]*$")){
            throw new IllegalArgumentException("닉네임 형식이 맞지 않습니다.");
        }
    }

    @Override
    @GetMapping("/emails/{email}")
    public ResponseEntity<LoginResponse> sameEmailCheck(@PathVariable(name="email") String email){
        memberService.checkEmailExist(email);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/nicknames/{nickname}")
    public ResponseEntity<LoginResponse> sameNicknameCheck(@PathVariable(name="nickname") String nickname){
        memberService.checkNicknameExist(nickname);
        return ResponseEntity.ok().build();
    }

}
