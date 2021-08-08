package com.moviePicker.api.member.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;

public interface MemberService {

    public Member getMemberByEmail(String email);

    public Member getMemberIfRegistered(String email, String password);

    public void createMember(MemberCreateRequest request);

    public void updateMember(Member loginMember, MemberUpdateRequest request);

    public void deleteMember(String nickname);

    public void checkEmailExist(String email);

    public void checkNicknameExist(String nickname);
}
