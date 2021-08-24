package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviePicker.api.comment.dto.CommentResponse;
import com.moviePicker.api.review.domain.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {

    private String movieCode;
    private String nickname;
    private String title;
    private String content;
    private Integer recommendationCount;
    private LocalDateTime createdAt;
    private List<CommentResponse> comments;

    @JsonCreator
    public ReviewResponse(Review review) {
        this.movieCode = review.getMovie().getMovieCode();
        this.nickname = review.getMember().getNickname();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.recommendationCount = review.getRecommendationCount();
        this.createdAt = review.getCreatedAt();
        this.comments = review.getCommentList().stream().map(CommentResponse::new).collect(Collectors.toList());
    }
}
