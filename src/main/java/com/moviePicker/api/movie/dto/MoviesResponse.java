package com.moviePicker.api.movie.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoviesResponse {

    private List<MovieResponse> movies;

    @JsonCreator
    public MoviesResponse(@JsonProperty("movies") List<MovieResponse> movies) {
        this.movies = movies;
    }
}
