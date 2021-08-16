package com.moviePicker.api.movieWished.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "wish_movies")
@Getter
public class MovieWished extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id", nullable = false, referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movies_id", nullable = false, referencedColumnName = "id")
    private Movie movie;

    protected MovieWished() {
    }

    private MovieWished(Member member, Movie movie) {
        this.member = member;
        this.movie = movie;
        member.getMovieWishedList().add(this);
    }

    public static MovieWished createMovieWished(Member member, Movie movie) {
        return new MovieWished(member, movie);
    }
}
