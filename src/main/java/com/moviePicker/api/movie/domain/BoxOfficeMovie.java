package com.moviePicker.api.movie.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "box_office_movies")
@Getter
public class BoxOfficeMovie {

    @Id
    @Column(name = "id")
    private String movieCode;

    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "subtitle", columnDefinition = "LONGTEXT")
    private String subtitle;

    @Column(name = "score", columnDefinition = "LONGTEXT")
    private String score;

    @Column(name = "genre", columnDefinition = "LONGTEXT")
    private String genre;

    @Column(name = "country", columnDefinition = "LONGTEXT")
    private String country;

    @Column(name = "running_time", columnDefinition = "LONGTEXT")
    private String runningTime;

    @Column(name = "pub_date", columnDefinition = "LONGTEXT")
    private String pubDate;

    @Column(name = "plot", columnDefinition = "LONGTEXT")
    private String plot;

    @Column(name = "film_rating", columnDefinition = "LONGTEXT")
    private String filmRating;

    @Column(name = "director", columnDefinition = "LONGTEXT")
    private String director;

    @Column(name = "actors", columnDefinition = "LONGTEXT")
    private String actors;

    @Column(name = "poster", columnDefinition = "LONGTEXT")
    private String poster;

    @Column(name = "still_cuts", columnDefinition = "LONGTEXT")
    private String stillCuts;

    protected BoxOfficeMovie() {
    }

    @Builder
    public BoxOfficeMovie(String movieCode, String title, String subtitle, String score, String genre, String country, String runningTime, String pubDate, String plot,
                          String filmRating, String director, String actors, String poster, String stillCuts) {

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
