package com.moviePicker.api.reviewReported.repository;

import com.moviePicker.api.reviewReported.domain.ReviewReported;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportedRepository extends JpaRepository<ReviewReported, Long> {
}
