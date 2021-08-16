package com.moviePicker.api.movie.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {

    private String movieCode;
    private String title;
    private String subtitle;
    private String score;
    private String genre;
    private String country;
    private String runningTime;
    private String pubDate;
    private String plot;
    private String filmRating;
    private String director;
    private String actors;
    private String poster;
    private String stillCuts;


    @Builder
    @JsonCreator
    public MovieResponse(

            @JsonProperty("movieCode") String movieCode,
            @JsonProperty("title") String title,
            @JsonProperty("subtitle") String subtitle,
            @JsonProperty("score") String score,
            @JsonProperty("genre") String genre,
            @JsonProperty("country") String country,
            @JsonProperty("runningTime") String runningTime,
            @JsonProperty("pubDate") String pubDate,
            @JsonProperty("plot") String plot,
            @JsonProperty("filmRating") String filmRating,
            @JsonProperty("director") String director,
            @JsonProperty("actors") String actors,
            @JsonProperty("poster") String poster,
            @JsonProperty("stillCuts") String stillCuts) {

        this.movieCode = movieCode;
        this.title = title;
        this.subtitle = subtitle;
        this.score = score;
        this.genre = genre;
        this.country = country;
        this.runningTime = runningTime;
        this.pubDate = pubDate;
        this.plot = plot;
        this.filmRating = filmRating;
        this.director = director;
        this.actors = actors;
        this.poster = poster;
        this.stillCuts = stillCuts;
    }


}
