package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;


@Getter
public class MemberUpdateRequest {

    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "올바른 형식(숫자, 문자, 특수문자 포함 8~15자리 이내)의 비밀번호를 입력해 주세요")
    private final String password;

    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "올바른 형식(숫자, 문자, 특수문자 포함 8~15자리 이내)의 비밀번호를 입력해 주세요")
    private final String passwordCheck;

    @Pattern(regexp = "^[가-힣]*$", message = "닉네임은 한글만 가능합니다.")
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
}
