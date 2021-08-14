package com.moviePicker.api.ReviewReported.repository;

import com.moviePicker.api.ReviewReported.domain.ReviewReported;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportedRepository extends JpaRepository<ReviewReported, Long> {
}
