package com.moviePicker.api.movie.controller;


import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.dto.MovieResponse;
import com.moviePicker.api.movie.dto.MoviesResponse;
import com.moviePicker.api.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/nowadays")
    public ResponseEntity<MoviesResponse> moviesRunning(@PageableDefault Pageable pageable) {

        List<MovieResponse> moviesRunning = movieService.searchMoviesRunning(pageable).stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesRunning));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<MoviesResponse> moviesByQuery(@PathVariable String query, @PageableDefault Pageable pageable) {
        List<MovieResponse> moviesByQuery = movieService.searchMoviesByQuery(query, pageable).stream()
                .map(MovieResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new MoviesResponse(moviesByQuery));
    }

    @GetMapping("/wish")
    public ResponseEntity<MoviesResponse> moviesWished(@AuthenticationPrincipal Member member, @PageableDefault Pageable pageable) {
        List<MovieResponse> moviesWished = movieService.searchMoviesWished(member, pageable).stream()
                .map(movieWished -> (new MovieResponse(movieWished.getMovie())))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesWished));
    }

    @GetMapping("/watched")
    public ResponseEntity<MoviesResponse> moviesWatched(@AuthenticationPrincipal Member member, @PageableDefault Pageable pageable) {
        List<MovieResponse> moviesWatched = movieService.searchMoviesWatched(member, pageable).stream()
                .map(movieWatched -> (new MovieResponse(movieWatched.getMovie())))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new MoviesResponse(moviesWatched));
    }

//    @GetMapping
//    public ResponseEntity<MovieResponse> movieByMovieId() {
//        Movie movie = movieService.searchMovieByMovieId();
//        return ResponseEntity.ok().body(new MovieResponse(movie));
//    }
//
//    @GetMapping
//    public ResponseEntity<MovieResponse> movieByReviewId() {
//        Movie movie = movieService.searchMovieByReviewId();
//        return ResponseEntity.ok().body(new MovieResponse(movie));
//    }
//
//    @PostMapping
//    public ResponseEntity<Boolean> registerMovieWished() {
//        return ResponseEntity.ok(movieService.registerMovieWished());
//    }
//
//    @PostMapping
//    public ResponseEntity<Boolean> registerMovieWatched() {
//        return ResponseEntity.ok(movieService.registerMovieWatched());
//    }


}
