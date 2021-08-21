package com.moviePicker.api.movie.service;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.BoxOfficeMovie;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.domain.MovieWatched;
import com.moviePicker.api.movie.domain.MovieWished;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {


    public List<BoxOfficeMovie> searchMoviesRunning(Pageable pageable);

    public List<Movie> searchMoviesByQuery(String query, Pageable pageable);

    public List<MovieWished> searchMoviesWished(Member member, Pageable pageable);

    public List<MovieWatched> searchMoviesWatched(Member member, Pageable pageable);

    public Movie searchMovieByMovieCode(String movieCode);

    public Movie searchMovieByReviewId(Long reviewId);

    public boolean registerMovieWished(Member member, String movieCode);

    public boolean registerMovieWatched(Member member, String movieCode);

}
