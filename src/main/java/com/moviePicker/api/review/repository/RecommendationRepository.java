package com.moviePicker.api.review.repository;

import com.moviePicker.api.review.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
