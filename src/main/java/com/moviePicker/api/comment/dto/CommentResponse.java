package com.moviePicker.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moviePicker.api.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {

    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;


    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.createdAt = comment.getCreatedAt();
    }

    @JsonCreator
    public CommentResponse(@JsonProperty("commentId") Long commentId,
                           @JsonProperty("content") String content,
                           @JsonProperty("nickname") String nickname,
                           @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
