package com.moviePicker.api.ReviewRecommended.repository;

import com.moviePicker.api.ReviewRecommended.domain.ReviewRecommended;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecommendedRepository extends JpaRepository<ReviewRecommended, Long> {
}
