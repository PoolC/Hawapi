package com.moviePicker.api.movie.domain;

import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "watched_movies")
@Getter
public class MovieWatched extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id", nullable = false, referencedColumnName = "id")
    private Movie movie;

    protected MovieWatched() {
    }

    private MovieWatched(Member member, Movie movie) {
        this.member = member;
        this.movie = movie;
        member.getMovieWatchedList().add(this);
    }

    public static MovieWatched of(Member member, Movie movie) {
        return new MovieWatched(member, movie);
    }
}
