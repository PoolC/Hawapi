package com.moviePicker.api.member.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.member.dto.MemberUpdateRequest;

public interface MemberService {

    public Member getMemberByEmail(String email);

    public Member getMemberIfRegistered(String email, String password);

    public boolean checkEmailExist(String email);

    public boolean checkNicknameExist(String nickname);

    public void create(MemberCreateRequest request);

    public void update(Member member, MemberUpdateRequest request);
    
    public void withdraw(Member member, String nickname);


}
