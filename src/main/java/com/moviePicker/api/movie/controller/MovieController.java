package com.moviePicker.api.movie.controller;


import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.dto.MovieResponse;
import com.moviePicker.api.movie.dto.MoviesResponse;
import com.moviePicker.api.movie.dto.WatchRegisterResponse;
import com.moviePicker.api.movie.dto.WishRegisterResponse;
import com.moviePicker.api.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

//    @GetMapping
//    public ResponseEntity<MoviesResponse> moviesRunning() {
//        List<MovieResponse> moviesRunning = movieService.searchMoviesRunning().stream()
//                .map(MovieResponse::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(new MoviesResponse(moviesRunning));
//    }
//
//    @GetMapping
//    public ResponseEntity<MoviesResponse> moviesByQuery() {
//        List<MovieResponse> moviesByQuery = movieService.searchMoviesByQuery().stream()
//                .map(MovieResponse::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(new MoviesResponse(moviesByQuery));
//    }
//
//    @GetMapping
//    public ResponseEntity<MoviesResponse> moviesWished() {
//        List<MovieResponse> moviesWished = movieService.searchMoviesWished().stream()
//                .map(MovieResponse::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(new MoviesResponse(moviesWished));
//    }
//
//    @GetMapping
//    public ResponseEntity<MoviesResponse> moviesWatched() {
//        List<MovieResponse> moviesWatched = movieService.searchMoviesWatched().stream()
//                .map(MovieResponse::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(new MoviesResponse(moviesWatched));
//    }

    @GetMapping(value = "/byMovieId/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> movieByMovieId(@PathVariable("movieId") String movieCode) {
        Movie movie = movieService.searchMovieByMovieCode(movieCode);
        return ResponseEntity.ok().body(new MovieResponse(movie));
    }

    @GetMapping(value="/byReviewId/{reviewsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponse> movieByReviewId(@PathVariable("reviewsId") Long reviewId) {
        Movie movie = movieService.searchMovieByReviewId(reviewId);
        return ResponseEntity.ok().body(new MovieResponse(movie));
    }

    @PostMapping(value="/wish/{movieId}")
    public ResponseEntity<WishRegisterResponse> registerMovieWished(@AuthenticationPrincipal Member member,@PathVariable("movieId") String movieCode) {
        return ResponseEntity.ok(new WishRegisterResponse(movieService.registerMovieWished(member,movieCode)));
    }

    @PostMapping(value="/watched/{movieId}")
    public ResponseEntity<WatchRegisterResponse> registerMovieWatched(@AuthenticationPrincipal Member member, @PathVariable("movieId") String movieCode) {
        return ResponseEntity.ok(new WatchRegisterResponse(movieService.registerMovieWatched(member,movieCode)));
    }

}
