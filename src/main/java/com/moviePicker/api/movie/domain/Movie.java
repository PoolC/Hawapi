package com.moviePicker.api.movie.domain;

import com.moviePicker.api.movieWatchMember.domain.MovieWatchMember;
import com.moviePicker.api.movieWishMember.domain.MovieWishMember;
import com.moviePicker.api.review.domain.Review;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="movies")
@Getter
public class Movie {

    @Id @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name="source_link", columnDefinition = "LONGTEXT")
    private String source_link;

    @Column(name="image_url", columnDefinition = "LONGTEXT")
    private String image_url;

    @OneToMany(mappedBy = "movie")
    List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<MovieWatchMember> movieWatchMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<MovieWishMember> movieWishMemberList = new ArrayList<>();

}
