package com.moviePicker.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentUpdateRequest {


    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder
    public CommentUpdateRequest(@JsonProperty("content") String content){
        this.content = content;
    }
}
