package com.moviePicker.api.reviewRecommended.repository;

import com.moviePicker.api.reviewRecommended.domain.ReviewRecommended;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecommendedRepository extends JpaRepository<ReviewRecommended, Long> {
}
