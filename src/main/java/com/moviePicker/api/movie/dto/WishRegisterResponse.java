package com.moviePicker.api.movie.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WishRegisterResponse {

    private Boolean isRegistered;

    @JsonCreator
    public WishRegisterResponse(@JsonProperty("isRegistered") boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
}
