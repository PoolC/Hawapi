package com.moviePicker.api.review.repository;

import com.moviePicker.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
