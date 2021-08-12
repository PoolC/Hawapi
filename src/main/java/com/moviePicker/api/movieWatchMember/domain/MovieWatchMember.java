package com.moviePicker.api.movieWatchMember.domain;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name="watched_movies")
@Getter
public class MovieWatchMember {

    @Id @GeneratedValue
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="member_id",nullable = false, referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="movie_id",nullable = false, referencedColumnName = "id")
    private Movie movie;
}
