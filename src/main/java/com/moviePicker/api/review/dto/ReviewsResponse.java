package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewsResponse {

    private List<ReviewResponse> reviews;

    public ReviewsResponse(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }
}
