package com.moviePicker.api.movie.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movieWatched.domain.MovieWatched;
import com.moviePicker.api.movieWished.domain.MovieWished;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MovieService {


    public List<Movie> searchMoviesRunning(Pageable pageable);

    public List<Movie> searchMoviesByQuery(String query, Pageable pageable);

    public List<MovieWished> searchMoviesWished(Member member, Pageable pageable);

    public List<MovieWatched> searchMoviesWatched(Member member, Pageable pageable);

    public Movie searchMovieByMovieCode(String movieCode);

    public Movie searchMovieByReviewId(Long reviewId);

    public boolean registerMovieWished(Member member, String movieCode);

    public boolean registerMovieWatched(Member member, String movieCode);

}
