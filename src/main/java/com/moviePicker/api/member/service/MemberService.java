package com.moviePicker.api.member.service;

import com.moviePicker.api.member.domain.Member;

public interface MemberService {

    public Member getMemberByEmail(String email);

    public Member getMemberIfRegistered(String email, String password);
}
