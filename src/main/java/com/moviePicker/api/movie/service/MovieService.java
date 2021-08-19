package com.moviePicker.api.movie.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    public List<Movie> searchMoviesRunning();

    public List<Movie> searchMoviesByQuery();

    public List<Movie> searchMoviesWished();

    public List<Movie> searchMoviesWatched();

    public Movie searchMovieByMovieCode(String movieCode);

    public Movie searchMovieByReviewId(Long reviewId);

    public boolean registerMovieWished(Member member, String movieCode);

    public boolean registerMovieWatched(Member member, String movieCode);

}
