package com.moviePicker.api.reviewRecommendMember.repository;

import com.moviePicker.api.reviewRecommendMember.domain.ReviewRecommendMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecommendMemberRepository extends JpaRepository<ReviewRecommendMember,Long> {
}
