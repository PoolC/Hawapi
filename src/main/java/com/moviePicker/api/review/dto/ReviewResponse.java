package com.moviePicker.api.review.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {

    String movieCode;
    String nickName;
    String title;
    String content;

}
