package com.moviePicker.api.movieWished.repository;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movieWished.domain.MovieWished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieWishedRepository extends JpaRepository<MovieWished, Long> {


    Optional<MovieWished> findByMemberAndMovie(Member member, Movie movie);

    void removeByMemberAndMovie(Member member, Movie movie);
}
