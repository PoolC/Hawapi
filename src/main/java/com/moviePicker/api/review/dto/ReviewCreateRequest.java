package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ReviewCreateRequest {

    private final String movieId;

    @Size(min = 1, max = 30, message = "제목은 1자이상 30자 이하로 작성해주세요")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요")
    private final String content;

    @JsonCreator
    @Builder
    public ReviewCreateRequest(
            @JsonProperty("movieId") String movieId,
            @JsonProperty("title") String title,
            @JsonProperty("content") String content){
        this.movieId = movieId;
        this.title = title;
        this.content = content;
    }

}