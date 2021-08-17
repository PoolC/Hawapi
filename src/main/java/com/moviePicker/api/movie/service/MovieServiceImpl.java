package com.moviePicker.api.movie.service;

import com.moviePicker.api.movie.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {


    @Override
    public List<Movie> searchMoviesRunning() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesByQuery() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesWished() {
        return null;
    }

    @Override
    public List<Movie> searchMoviesWatched() {
        return null;
    }

    @Override
    public Movie searchMovieByMovieId() {
        return null;
    }

    @Override
    public Movie searchMovieByReviewId() {
        return null;
    }

    @Override
    public boolean registerMovieWished() {
        return false;
    }

    @Override
    public boolean registerMovieWatched() {
        return false;
    }
}
