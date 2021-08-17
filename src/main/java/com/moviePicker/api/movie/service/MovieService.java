package com.moviePicker.api.movie.service;

import com.moviePicker.api.movie.domain.Movie;

import java.util.List;

public interface MovieService {


    public List<Movie> searchMoviesRunning();

    public List<Movie> searchMoviesByQuery();

    public List<Movie> searchMoviesWished();

    public List<Movie> searchMoviesWatched();

    public Movie searchMovieByMovieId();

    public Movie searchMovieByReviewId();

    public boolean registerMovieWished();

    public boolean registerMovieWatched();


}
