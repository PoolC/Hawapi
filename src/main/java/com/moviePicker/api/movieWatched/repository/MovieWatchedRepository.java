package com.moviePicker.api.movieWatched.repository;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWished.domain.MovieWished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieWatchedRepository extends JpaRepository<MovieWatched, Long> {

    Optional<MovieWatched> findByMemberAndMovie(Member member, Movie movie);

    void removeByMemberAndMovie(Member member,Movie movie);
}
