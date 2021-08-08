package com.moviePicker.api.member.controller;

import com.moviePicker.api.auth.dto.LoginResponse;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface MemberAcceptenceControlller {
    public ResponseEntity<LoginResponse> createMember(MemberCreateRequest request);
    public ResponseEntity<LoginResponse> updateMember(Member member, MemberUpdateRequest request);
    public ResponseEntity<LoginResponse> withdrawMember(Member member, String nickname);
    public ResponseEntity<LoginResponse> sameEmailCheck(String email);
    public ResponseEntity<LoginResponse> sameNicknameCheck(String nickname);
}
