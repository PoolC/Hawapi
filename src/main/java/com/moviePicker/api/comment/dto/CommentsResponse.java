package com.moviePicker.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsResponse {

    private List<CommentResponse> comments;

    @JsonCreator
    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
