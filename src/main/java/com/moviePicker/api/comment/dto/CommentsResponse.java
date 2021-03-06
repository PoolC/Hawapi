package com.moviePicker.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsResponse {

    private List<CommentResponse> comments;

    @JsonCreator
    public CommentsResponse(@JsonProperty("comments") List<CommentResponse> comments) {
        this.comments = comments;
    }
}
