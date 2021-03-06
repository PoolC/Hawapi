package com.moviePicker.api.review.repository;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.review.domain.Recommendation;
import com.moviePicker.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {


    Optional<Recommendation> findByMemberAndReview(Member member, Review review);

    
}
