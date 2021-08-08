package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moviePicker.api.common.exception.NotSameException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {

    private final String password;
    private final String passwordCheck;
    private final String nickname;


    @JsonCreator
    @Builder
    public MemberUpdateRequest(
            @JsonProperty("password") String password,
            @JsonProperty("passwordCheck") String passwordCheck,
            @JsonProperty("nickname") String nickname) {
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }

    public void checkPasswordFormat(){
        //영문 + 숫자 + 특수문자 조합 정규식
        if(!this.password.matches("((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,})")){
            throw new IllegalArgumentException("비밀번호 형식이 맞지 않습니다.");
        }
    }
}
