package com.moviePicker.api.review.repository;

import com.moviePicker.api.member.domain.Member;
import com.moviePicker.api.review.domain.Report;
import com.moviePicker.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByMemberAndReview(Member member, Review review);

    Void deleteByMemberAndReview(Member member, Review review);

}
