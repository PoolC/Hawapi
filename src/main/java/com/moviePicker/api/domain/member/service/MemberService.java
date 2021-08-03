package com.moviePicker.api.domain.member.service;

import com.moviePicker.api.domain.member.domain.Member;

public interface MemberService {

    public Member getMemberByEmail(String email);

    public Member getMemberIfRegistered(String email, String password);
}
