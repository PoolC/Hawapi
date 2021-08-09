package com.moviePicker.api.member.controller;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import com.moviePicker.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/emails/{email}")
    public ResponseEntity<Boolean> isEmailDuplicated(@PathVariable String email) {
        return ResponseEntity.ok(memberService.checkEmailExist(email));
    }

    @GetMapping("/nicknames/{nickname}")
    public ResponseEntity<Boolean> isNicknameDuplicated(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.checkNicknameExist(nickname));
    }


    @PostMapping
    public ResponseEntity<Void> memberCreate(@RequestBody @Valid MemberCreateRequest request) {

        memberService.create(request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/me")
    public ResponseEntity<Void> memberUpdate(@AuthenticationPrincipal Member member, @RequestBody @Valid MemberUpdateRequest request) {

        memberService.update(member, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{nickname}")
    public ResponseEntity<Void> memberWithdraw(@AuthenticationPrincipal Member member, @PathVariable String nickname) {
        memberService.withdraw(member, nickname);
        return ResponseEntity.ok().build();
    }
}
