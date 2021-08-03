package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.moviePicker.api.common.exception.NotSameException;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class MemberCreateRequest {

    private final String name;
    private final String email;
    private final String password;
    private final String passwordCheck;
    private final String nickname;


    @JsonCreator
    @Builder
    public MemberCreateRequest(String name, String email, String password, String passwordCheck, String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }

    public void checkRequestValid() {

        checkNameValid();
        checkEmailValid();
        checkNicknameValid();
        checkPasswordValid();

    }



    private void checkPasswordValid() {
        Optional.ofNullable(password)
                .orElseThrow(() -> new IllegalArgumentException("패스워드를 입력하지 않았습니다."));
        Optional.ofNullable(passwordCheck)
                .orElseThrow(() -> new IllegalArgumentException("패스워드 체크를 작성하지 않았습니다."));

        if(password.equals(""))
            throw new IllegalArgumentException("비밀번호를 작성하지 않았습니다.");
        if(passwordCheck.equals(""))
            throw new IllegalArgumentException("비밀번호체크를 작성하지 않았습니다.");


        if (!password.equals(passwordCheck)) {
            throw new NotSameException("비밀번호와 비밀번호 체크가 틀렸습니다.");
        }
    }

    private void checkNicknameValid() {


        Optional.ofNullable(nickname)
                .orElseThrow(() -> new IllegalArgumentException("닉네임을 작성하지 않았습니다."));
        if(nickname.equals("") ) {
            throw new IllegalArgumentException("닉네임을 작성하지 않았습니다.");
        }
    }

    private void checkEmailValid() {
        Optional.ofNullable(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 작성하지 않았습니다."));
        if(email.equals("") ) {
            throw new IllegalArgumentException("이메일을 작성하지 않았습니다.");
        }
    }

    private void checkNameValid() {
        Optional.ofNullable(name)
                .orElseThrow(() -> new IllegalArgumentException("이름을 작성하지 않았습니다."));
        if(name.equals("") ){
            throw new IllegalArgumentException("이름을 작성하지 않았습니다.");}
    }


}
