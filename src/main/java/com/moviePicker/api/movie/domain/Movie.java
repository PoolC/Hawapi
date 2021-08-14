package com.moviePicker.api.movie.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "movies")
@Getter
public class Movie extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "movie_code", columnDefinition = "LONGTEXT", nullable = false, unique = true)
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

    @OneToMany(mappedBy = "movie")
    List<Review> reviewList = new ArrayList<>();

    protected Movie() {
    }

    @Builder
    public Movie(Long id, String movieCode, String title, String subtitle, String score, String genre, String country, String runningTime, String pubDate, String plot,
                 String filmRating, String director, String actors, String poster, String stillCuts, List<Review> reviewList) {
        this.id = id;
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
        this.reviewList = reviewList;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieCode='" + movieCode + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", score='" + score + '\'' +
                ", genre='" + genre + '\'' +
                ", country='" + country + '\'' +
                ", runningTime='" + runningTime + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", plot='" + plot + '\'' +
                ", filmRating='" + filmRating + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", stillCuts='" + stillCuts + '\'' +
                ", reviewList=" + reviewList +
                '}';
    }
}
