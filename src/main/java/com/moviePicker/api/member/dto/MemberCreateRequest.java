package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
public class MemberCreateRequest {


    @NotBlank(message = "이름을 입력해 주세요")
    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요")
    private final String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 형식의 이메일을 입력해 주세요.")
    @javax.validation.constraints.Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "올바른 형식의 이메일을 입력해 주세요")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @javax.validation.constraints.Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "올바른 형식(숫자, 문자, 특수문자 포함 8~15자리 이내)의 비밀번호를 입력해 주세요")
    private final String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    @javax.validation.constraints.Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "올바른 형식(숫자, 문자, 특수문자 포함 8~15자리 이내)의 비밀번호를 입력해 주세요")
    private final String passwordCheck;


    @NotBlank(message = "닉네임을 입력해 주세요")
    @javax.validation.constraints.Pattern(regexp = "^[가-힣]*$", message = "닉네임은 한글만 가능합니다.")
    private final String nickname;


    @JsonCreator
    @Builder
    public MemberCreateRequest(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("passwordCheck") String passwordCheck,
            @JsonProperty("nickname") String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }


}
