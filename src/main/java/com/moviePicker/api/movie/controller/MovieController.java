package com.moviePicker.api.movie.controller;


import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.dto.MovieResponse;
import com.moviePicker.api.movie.dto.MoviesResponse;
import com.moviePicker.api.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<MoviesResponse> moviesRunning() {
        List<MovieResponse> moviesRunning = movieService.searchMoviesRunning().stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesRunning));
    }

    @GetMapping
    public ResponseEntity<MoviesResponse> moviesByQuery() {
        List<MovieResponse> moviesByQuery = movieService.searchMoviesByQuery().stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesByQuery));
    }

    @GetMapping
    public ResponseEntity<MoviesResponse> moviesWished() {
        List<MovieResponse> moviesWished = movieService.searchMoviesWished().stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesWished));
    }

    @GetMapping
    public ResponseEntity<MoviesResponse> moviesWatched() {
        List<MovieResponse> moviesWatched = movieService.searchMoviesWatched().stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesWatched));
    }

    @GetMapping
    public ResponseEntity<MovieResponse> movieByMovieId() {
        Movie movie = movieService.searchMovieByMovieId();
        return ResponseEntity.ok().body(new MovieResponse(movie));
    }

    @GetMapping
    public ResponseEntity<MovieResponse> movieByReviewId() {
        Movie movie = movieService.searchMovieByReviewId();
        return ResponseEntity.ok().body(new MovieResponse(movie));
    }

    @PostMapping
    public ResponseEntity<Boolean> registerMovieWished() {
        return ResponseEntity.ok(movieService.registerMovieWished());
    }

    @PostMapping
    public ResponseEntity<Boolean> registerMovieWatched() {
        return ResponseEntity.ok(movieService.registerMovieWatched());
    }


}
