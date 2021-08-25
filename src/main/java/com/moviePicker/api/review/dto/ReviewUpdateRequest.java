package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUpdateRequest {

    private final String title;

    private final String content;

    @JsonCreator
    @Builder
    public ReviewUpdateRequest(
            @JsonProperty("title") String title,
            @JsonProperty("content") String content){
        this.title = title;
        this.content = content;
    }

}