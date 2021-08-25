package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    protected ReviewResponse() {
    }


    public ReviewResponse(Review review) {
        this.movieCode = review.getMovie().getMovieCode();
        this.nickname = review.getMember().getNickname();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.recommendationCount = review.getRecommendationCount();
        this.createdAt = review.getCreatedAt();
        this.comments = review.getCommentList().stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    @JsonCreator
    public ReviewResponse(@JsonProperty("movieCode") String movieCode,
                          @JsonProperty("nickname") String nickname,
                          @JsonProperty("title") String title,
                          @JsonProperty("content") String content,
                          @JsonProperty("recommendationCount") Integer recommendationCount,
                          @JsonProperty("createdAt") LocalDateTime createdAt,
                          @JsonProperty("comments") List<CommentResponse> comments) {

        this.movieCode = movieCode;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.recommendationCount = recommendationCount;
        this.createdAt = createdAt;
        this.comments = comments;

    }
}
