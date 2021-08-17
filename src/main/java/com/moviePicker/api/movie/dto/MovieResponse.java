package com.moviePicker.api.movie.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviePicker.api.movie.domain.Movie;
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
    public MovieResponse(Movie movie) {

        this.movieCode = movie.getMovieCode();
        this.title = movie.getTitle();
        this.subtitle = movie.getSubtitle();
        this.score = movie.getScore();
        this.genre = movie.getGenre();
        this.country = movie.getCountry();
        this.runningTime = movie.getRunningTime();
        this.pubDate = movie.getPubDate();
        this.plot = movie.getPlot();
        this.filmRating = movie.getFilmRating();
        this.director = movie.getDirector();
        this.actors = movie.getActors();
        this.poster = movie.getPoster();
        this.stillCuts = movie.getStillCuts();
    }


}
