package com.moviePicker.api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class AuthorizationTokenRequest {
    private final String authorizationToken;

    @JsonCreator
    public AuthorizationTokenRequest(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }
}
