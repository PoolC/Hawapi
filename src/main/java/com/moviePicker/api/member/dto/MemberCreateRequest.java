package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moviePicker.api.common.exception.NotSameException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.aop.scope.ScopedProxyUtils;

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

    public void checkColumn() {
        checkName();
        checkEmail();
        checkPassword();
        checkPasswordCheck();
        checkNickname();
    }

    private void checkName() {
        if (this.name.equals("")) {
            throw new IllegalArgumentException("이름을 작성하지 않았습니다.");
        }
    }

    private void checkEmail() {
        if(this.email.equals("")){
            throw new IllegalArgumentException("이메일을 작성하지 않았습니다.");
        }
    }

    private void checkPassword() {
        if(this.password.equals("")){
            throw new IllegalArgumentException("패스워드을 작성하지 않았습니다.");
        }
    }

    private void checkPasswordCheck() {
        if(this.passwordCheck.equals("")){
            throw new IllegalArgumentException("패스워드체크을 작성하지 않았습니다.");
        }
    }

    private void checkNickname() {
        if(this.nickname.equals("")){
            throw new IllegalArgumentException("닉네임을 작성하지 않았습니다.");
        }
    }

    public void checkPasswordSame(){
        if(!this.password.equals(this.passwordCheck)){
            throw new NotSameException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }

    public void checkEmailFormat(){
        //99%의 이메일형식을 찾는 이메일 정규식
        if(!this.email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
            throw new IllegalArgumentException("이메일 형식이 맞지 않습니다.");
        }
    }
}
