package com.moviePicker.api.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class MemberUpdateRequest {


    private final String email;
    private final String password;
    private final String passwordCheck;
    private final String nickname;


    @JsonCreator
    @Builder
    public MemberUpdateRequest(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("passwordCheck") String passwordCheck,
            @JsonProperty("nickname") String nickname) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }
}
