package com.moviePicker.api.review.repository;

import com.moviePicker.api.review.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
