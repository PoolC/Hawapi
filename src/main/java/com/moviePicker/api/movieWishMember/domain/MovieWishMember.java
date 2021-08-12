package com.moviePicker.api.movieWishMember.domain;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name="wish_movies")
@Getter
public class MovieWishMember {

    @Id @GeneratedValue
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="members_id", nullable = false, referencedColumnName="id")
    Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="movies_id", nullable = false, referencedColumnName = "id")
    Movie movie;
}
