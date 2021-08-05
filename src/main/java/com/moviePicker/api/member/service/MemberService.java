package com.moviePicker.api.member.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;

public interface MemberService {

    public Member getMemberByEmail(String email);

    public Member getMemberIfRegistered(String email, String password);

    public void create(MemberCreateRequest request);
}
