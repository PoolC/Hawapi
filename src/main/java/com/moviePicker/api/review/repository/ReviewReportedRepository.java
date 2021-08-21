package com.moviePicker.api.review.repository;

import com.moviePicker.api.review.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportedRepository extends JpaRepository<Report, Long> {
}
