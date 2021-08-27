package com.moviePicker.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CommentCreateRequest {

    private Long reviewId;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 1, max = 1000, message = "댓글은 1자이상 1000자 이하 작성 가능합니다.")
    private String content;

    @Builder
    public CommentCreateRequest(
            @JsonProperty("reviewId") Long reviewId,
            @JsonProperty("content") String content){
        this.reviewId = reviewId;
        this.content = content;
    }
}
