package com.moviePicker.api.review.controller;


import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.member.dto.MemberCreateRequest;
import com.moviePicker.api.movie.domain.Movie;
import com.moviePicker.api.movie.repository.MovieRepository;
import com.moviePicker.api.review.domain.Review;
import com.moviePicker.api.review.dto.ReviewCreateRequest;
import com.moviePicker.api.review.dto.ReviewResponse;
import com.moviePicker.api.review.dto.ReviewUpdateRequest;
import com.moviePicker.api.review.dto.ReviewsResponse;
import com.moviePicker.api.review.repository.ReviewRepository;
import com.moviePicker.api.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<Void> createReview(@AuthenticationPrincipal Member member,@RequestBody @Valid ReviewCreateRequest request){
        reviewService.createReview(member,request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@AuthenticationPrincipal Member member,@RequestBody @Valid ReviewUpdateRequest request,@PathVariable("reviewId") Long reviewId){
        reviewService.updateReview(member,request,reviewId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal Member member,@PathVariable("reviewId") Long reviewId){
        reviewService.deleteReview(member,reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> findReviewByReviewId(@PathVariable("reviewId") Long reviewId){
        return ResponseEntity.ok().body(new ReviewResponse((reviewService.searchByReviewId(reviewId))));
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<ReviewsResponse> searchReviewsByMovieId(@PathVariable("movieId") String movieCode) {
        Movie movie = findMovie(movieCode);
        return ResponseEntity.ok().body(new ReviewsResponse(reviewService.searchReviewByMovieId(movie)));
    }

    @GetMapping(value = "/me/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewsResponse> searchMyReviewsByMovieId(@AuthenticationPrincipal Member member, @PathVariable("movieId") String movieCode) {
        Movie movie = findMovie(movieCode);
        return ResponseEntity.ok().body(new ReviewsResponse(reviewService.searchMyReviewByMovieId(member, movie)));
    }

    @PostMapping(value = "/recommend/{reviewId}")
    public ResponseEntity<Void> recommendReview(@AuthenticationPrincipal Member member, @PathVariable("reviewId") Long reviewId) {
        Review review = findReview(reviewId);
        reviewService.recommendReview(member, review);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/report/{reviewId}")
    public ResponseEntity<Void> reportReview(@AuthenticationPrincipal Member member, @PathVariable("reviewId") Long reviewId) {
        Review review = findReview(reviewId);
        reviewService.reportReview(member, review);
        return ResponseEntity.ok().build();
    }


    private Movie findMovie(String movieCode) {
        Optional<Movie> movie = movieRepository.findById(movieCode);
        return movie.orElseThrow(() -> new NoSuchElementException("존재하지 않는 영화입니다,"));

    }

    private Review findReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.orElseThrow(() -> new NoSuchElementException("존재하지 않는 리뷰입니다."));
    }


}
