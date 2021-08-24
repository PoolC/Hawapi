package com.moviePicker.api.review.controller;


import com.moviePicker.api.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;


    //    @PostMapping()
//    public ResponseEntity<Void> createReview() {
//
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<Void> updateReview() {
//
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview() {
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/{reviewId}")
//    public ResponseEntity<ReviewResponse> searchReviewByReviewId() {
//
//
//        return ResponseEntity.ok().body(new ReviewResponse());
//    }
//
//    @GetMapping("/movies/{movieId}")
//    public ResponseEntity<ReviewsResponse> searchReviewsByMovieId() {
//        return ResponseEntity.ok().body(new ReviewsResponse());
//    }
//
//    @GetMapping(value = "/reviews/me/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ReviewsResponse> searchMyReviewsByMovieId() {
//
//        return ResponseEntity.ok().body(new ReviewsResponse());
//    }
//
//    @PostMapping(value = "/recommend/{reviewId}")
//    public ResponseEntity<Void> recommendReview() {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping(value = "/report/{reviewId}")
//    public ResponseEntity<Void> reportReview() {
//        return ResponseEntity.ok().build();
//    }


}
